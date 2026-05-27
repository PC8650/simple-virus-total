package com.vt.flow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ai 提供方 自定义配置
 */
@Data
@ConfigurationProperties(prefix = "ai-provider")
public class AiProviderConfig {

    private boolean customer;

    private String baseUrl;

    private String apiKey;

    private String model;

    private Float temperature;

}
