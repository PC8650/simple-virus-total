package com.vt.config;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    @Value("${params.v-key:}")
    private String apiKey;

    @Bean
    public HttpRequestFactory httpRequestFactory() {
        HttpTransport transport = new NetHttpTransport();
        return transport.createRequestFactory();
    }

    @Bean
    public HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("x-apikey", apiKey);
        return headers;
    }
}
