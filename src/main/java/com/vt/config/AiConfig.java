package com.vt.config;

import com.vt.flow.advisor.AnalyseAdvisor;
import com.vt.flow.advisor.ReportAdvisor;
import com.vt.flow.advisor.ScanAdvisor;
import com.vt.flow.advisor.SummaryAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 核心配置类
 * 负责构建和编排全局的 ChatClient 工作流
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient vtChatClient(ChatClient.Builder builder,
                                   ScanAdvisor scanAdvisor,
                                   AnalyseAdvisor analyseAdvisor,
                                   ReportAdvisor reportAdvisor,
                                   SummaryAdvisor summaryAdvisor) {

        return builder
                .defaultAdvisors(
                        scanAdvisor,
                        analyseAdvisor,
                        reportAdvisor,
                        summaryAdvisor
                )
                .build();
    }
}
