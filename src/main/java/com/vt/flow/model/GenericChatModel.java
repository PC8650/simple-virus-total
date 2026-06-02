package com.vt.flow.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vt.enums.MsgEnum;
import com.vt.exception.WrapperException;
import com.vt.utils.MessageUtils;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.util.List;
import java.util.Map;

/**
 * 零依赖的 OpenAI 协议适配器 (支持同步与流式 Flux)
 */
@Slf4j
public class GenericChatModel implements ChatModel {

    private final WebClient webClient;
    private final String model;
    private final float temperature;
    private final Gson gson;

    public GenericChatModel(String baseUrl, String apiKey, String model, float temperature, Gson gson) {
        this.model = model;
        this.temperature = temperature;
        this.gson = gson;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)))
                .build();
    }

    @NotNull
    @Override
    public ChatResponse call(Prompt prompt) {
        Map<String, Object> body = buildRequestBody(prompt, false);

        try {
            String responseStr = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (!StringUtils.hasText(responseStr)) {
                throw new WrapperException(MessageUtils.getMessage(MsgEnum.LLM_ERR_EMPTY_RESPONSE));
            }

            // 一次性使用 Gson 解析
            JsonObject responseJson = gson.fromJson(responseStr, JsonObject.class);

            JsonArray choices = responseJson.getAsJsonArray("choices");
            String content = choices.get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

            ChatResponseMetadata metadata = buildMetadata(responseJson);

            return new ChatResponse(List.of(new Generation(new AssistantMessage(content))), metadata);

        } catch (WebClientResponseException e) {
            log.error("LLM API synchronization call failed! HTTP Status: {}, Response Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.LLM_ERR_SYNC_FAILED), e);
        }
    }

    @NotNull
    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        Map<String, Object> body = buildRequestBody(prompt, true);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("LLM API stream call failed! HTTP Status: {}, Response Body: {}",
                            e.getStatusCode(), e.getResponseBodyAsString());
                    throw new WrapperException(MessageUtils.getMessage(MsgEnum.LLM_ERR_STREAM_FAILED), e);
                })
                // 1. 过滤掉 SSE 协议中可能存在的空行 (Keep-alive ping)
                .filter(StringUtils::hasText)
                // 2. 不是OpenAI 流结束的标志 "[DONE]"就一直处理
                .takeWhile(data -> !"[DONE]".equals(data))
                .map(this::parseStreamChunk);
    }

    @Override
    public ChatOptions getDefaultOptions() {
        return null;
    }

    private Map<String, Object> buildRequestBody(Prompt prompt, boolean isStream) {
        List<Map<String, String>> messages = prompt.getInstructions().stream()
                .map(m -> Map.of("role", m.getMessageType().getValue(), "content", m.getText()))
                .toList();

        if (isStream) {
            return Map.of(
                    "model", model,
                    "messages", messages,
                    "temperature", temperature,
                    "stream", true,
                    "stream_options", Map.of("include_usage", true));
        }

        return Map.of(
                "model", model,
                "messages", messages,
                "temperature", temperature,
                "stream", false);
    }

    private ChatResponse parseStreamChunk(String data) {
        StringBuilder contentBuilder = new StringBuilder();
        ChatResponseMetadata metadata = null;
        try {
            JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

            JsonArray choices = jsonObject.getAsJsonArray("choices");
            if (choices != null && !choices.isEmpty()) {
                JsonObject delta = choices.get(0).getAsJsonObject().getAsJsonObject("delta");
                if (delta != null) {
                    JsonElement reasoningEl = delta.get("reasoning_content");
                    if (reasoningEl != null && !reasoningEl.isJsonNull()) {
                        contentBuilder.append(reasoningEl.getAsString());
                    }

                    JsonElement contentEl = delta.get("content");
                    if (contentEl != null && !contentEl.isJsonNull()) {
                        contentBuilder.append(contentEl.getAsString());
                    }
                }
            }

            metadata = buildMetadata(jsonObject);
        } catch (Exception e) {
            log.error("Failed to parse stream chunk: {}", data, e);
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.LLM_ERR_PARSE_CHUNK_FAILED), e);
        }

        return new ChatResponse(List.of(new Generation(new AssistantMessage(contentBuilder.toString()))), metadata);
    }

    private ChatResponseMetadata buildMetadata(JsonObject jsonObject) {
        Usage usage = null;
        JsonElement usageElement = jsonObject.get("usage");

        if (usageElement != null && !usageElement.isJsonNull() && usageElement.isJsonObject()) {
            JsonObject usageJson = usageElement.getAsJsonObject();

            int promptTokens = getAsInt(usageJson.get("prompt_tokens"));
            int completionTokens = getAsInt(usageJson.get("completion_tokens"));
            int totalTokens = getAsInt(usageJson.get("total_tokens"));

            usage = new DefaultUsage(promptTokens, completionTokens, totalTokens);
        }

        return ChatResponseMetadata.builder()
                .usage(usage)
                .build();
    }

    private int getAsInt(JsonElement el) {
        return (el instanceof JsonPrimitive p && p.isNumber()) ? p.getAsInt() : 0;
    }

}