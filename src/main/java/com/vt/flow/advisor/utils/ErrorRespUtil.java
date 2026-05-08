package com.vt.flow.advisor.utils;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.util.List;

/**
 * Advisor Chain 错误响应工具
 */
public class ErrorRespUtil {

    private ErrorRespUtil(){}

    public static ChatClientResponse buildErrorResp(ChatClientRequest chatClientRequest, String errorMessage) {
        ChatResponse chatResponse = new ChatResponse(List.of(
                new Generation(new AssistantMessage(errorMessage))
        ));
        return ChatClientResponse.builder()
                .chatResponse(chatResponse)
                .context(chatClientRequest.context())
                .build();
    }

}
