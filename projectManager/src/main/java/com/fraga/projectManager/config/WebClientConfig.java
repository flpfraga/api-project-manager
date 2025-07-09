package com.fraga.projectManager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${member.api.base-url}")
    private String memberApiBaseUrl;

    @Value("${member.api.timeout}")
    private Duration timeout;

    @Bean("memberWebClient")
    public WebClient memberWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(memberApiBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(timeout)
                ))
                .build();
    }
}
