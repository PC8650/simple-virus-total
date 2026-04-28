package com.vt.atp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GsonConfig implements WebMvcConfigurer{

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .serializeNulls()
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)  // 读取时保持精度
                .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)  // 写入时保持原格式
                .create();
    }


    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        WebMvcConfigurer.super.configureMessageConverters(builder.withJsonConverter(new GsonHttpMessageConverter(gson())));
    }

}
