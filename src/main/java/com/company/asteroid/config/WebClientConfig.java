package com.company.asteroid.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final NasaProperties nasaProperties;

    @Bean
    public WebClient nasaWebClient() {

        HttpClient httpClient = HttpClient.create()
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        5000
                )
                .responseTimeout(
                        Duration.ofSeconds(10)
                )
                .doOnConnected(connection ->
                        connection.addHandlerLast(
                                new ReadTimeoutHandler(
                                        10,
                                        TimeUnit.SECONDS
                                )
                        )
                );

        return WebClient.builder()
                .baseUrl(
                        nasaProperties.getBaseUrl()
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .clientConnector(
                        new ReactorClientHttpConnector(
                                httpClient
                        )
                )
                .build();
    }
}
