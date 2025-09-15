package com.dahuaboke.mpda.client;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

/**
 * @Desc: HTTP 发送配置
 * @Author：zhh
 * @Date：2025/9/11 15:53
 */
@AutoConfiguration
public class ClientConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public CustomClient customClient(){
        Consumer<HttpHeaders> finalHeaders = (h) -> {
            h.setContentType(MediaType.APPLICATION_JSON);
        };
        return new CustomClient(RestClient.builder().defaultHeaders(finalHeaders).build(),
                WebClient.builder().defaultHeaders(finalHeaders).build());
    }
}
