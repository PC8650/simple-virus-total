package com.vt.flow.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
        // 使用 WebClient 替代 RestClient，以完美支持 Flux 和 SSE
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public ChatResponse call(Prompt prompt) {
        Map<String, Object> body = buildRequestBody(prompt, false);

        try {
            Map<String, Object> response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            assert response != null;
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            String content = (String) ((Map<String, Object>) choices.getFirst().get("message")).get("content");

            return new ChatResponse(List.of(new Generation(new AssistantMessage(content))));

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            log.error("LM API synchronization call failed! HTTP Status: {}, Response Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    @NotNull
    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        Map<String, Object> body = buildRequestBody(prompt, true);

        // 流式请求
        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .accept(MediaType.TEXT_EVENT_STREAM) // 声明接收 SSE 流
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(org.springframework.web.reactive.function.client.WebClientResponseException.class, e -> {
                    log.error("LLM API stream call failed! HTTP Status: {}, Response Body: {}",
                            e.getStatusCode(), e.getResponseBodyAsString());
                })
                // 过滤掉空行和 OpenAI 流结束的标志 "[DONE]"
                .filter(data -> StringUtils.hasText(data) && !"[DONE]".equals(data.trim()))
                .map(this::parseStreamChunk);
    }

    @Override
    public ChatOptions getDefaultOptions() {
        // 简单实现，暂不处理动态 Options
        return null;
    }

    /**
     * 组装 OpenAI 格式的请求体
     */
    private Map<String, Object> buildRequestBody(Prompt prompt, boolean isStream) {
        List<Map<String, String>> messages = prompt.getInstructions().stream()
                .map(m -> Map.of("role", m.getMessageType().getValue(), "content", m.getText()))
                .toList();

        return Map.of(
                "model", model,
                "messages", messages,
                "temperature", temperature,
                "stream", isStream // 动态控制是否开启流式
        );
    }

    /**
     * 解析 OpenAI 流式返回的 Chunk 数据
     * 格式示例: {"choices":[{"delta":{"content":"Hello"}}]}
     */
    private ChatResponse parseStreamChunk(String data) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
            JsonArray choices = jsonObject.getAsJsonArray("choices");

            if (choices != null && !choices.isEmpty()) {
                JsonObject delta = choices.get(0).getAsJsonObject().getAsJsonObject("delta");
                if (delta != null) {
                    // 处理深度思考内容 (Reasoning Content)
                    if (delta.has("reasoning_content") && !delta.get("reasoning_content").isJsonNull()) {
                        String reasoning = delta.get("reasoning_content").getAsString();
                        // 可选：如果你想在前端展示思考过程，可以加上特定标记
                        // contentBuilder.append("<think>").append(reasoning).append("</think>");
                        contentBuilder.append(reasoning);
                    }

                    //安全处理正式内容 (Content)
                    if (delta.has("content") && !delta.get("content").isJsonNull()) {
                        contentBuilder.append(delta.get("content").getAsString());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse stream chunk: {}", data, e);
        }

        // 封装为 Spring AI 的标准响应格式
        return new ChatResponse(List.of(new Generation(new AssistantMessage(contentBuilder.toString()))));
    }

}