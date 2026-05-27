package com.vt.flow.config;

import com.google.gson.Gson;
import com.vt.enums.MsgEnum;
import com.vt.flow.advisor.AnalyseAdvisor;
import com.vt.flow.advisor.ReportAdvisor;
import com.vt.flow.advisor.ScanAdvisor;
import com.vt.flow.advisor.SummaryAdvisor;
import com.vt.flow.model.GenericChatModel;
import com.vt.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 核心配置类
 * 负责构建和编排全局的 ChatClient 工作流
 */
@Configuration
@RequiredArgsConstructor
public class AiConfig {

    @Bean
    public ChatClient vtChatClient(
            Gson gson,
            AiProviderConfig aiProviderConfig,
            ObjectProvider<ChatModel> defaultChatModelProvider, // 获取 Starter 自动配置的模型 (如 Google)
            ScanAdvisor scanAdvisor,
            AnalyseAdvisor analyseAdvisor,
            ReportAdvisor reportAdvisor,
            SummaryAdvisor summaryAdvisor) {

        ChatModel activeChatModel;

        // 1. 判断是否启用了自定义厂商
        if (aiProviderConfig.isCustomer()) {
            // 实例化自定义简化的 OpenAI 协议适配器
            activeChatModel = new GenericChatModel(
                    aiProviderConfig.getBaseUrl(),
                    aiProviderConfig.getApiKey(),
                    aiProviderConfig.getModel(),
                    aiProviderConfig.getTemperature(),
                    gson
            );
        } else {
            // 使用 Spring AI Starter 自动配置的默认模型 (Google GenAI)
            activeChatModel = defaultChatModelProvider.getIfAvailable();
            if (activeChatModel == null) {
                throw new IllegalStateException(MessageUtils.getMessage(MsgEnum.SYS_AI_CONFIG_ERROR));
            }
        }

        // 2. 使用选定的 ChatModel 构建 ChatClient，并挂载所有的 Advisor
        return ChatClient.builder(activeChatModel)
                .defaultAdvisors(
                        scanAdvisor,
                        analyseAdvisor,
                        reportAdvisor,
                        summaryAdvisor
                )
                .build();
    }

}