package com.company.asteroid.client;

import com.company.asteroid.config.NasaProperties;
import com.company.asteroid.dto.nasa.NasaFeedResponse;
import com.company.asteroid.dto.nasa.NasaLookupResponse;
import com.company.asteroid.exception.NasaApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class NasaClientImpl implements NasaClient{

    private final WebClient nasaWebClient;

    private final NasaProperties nasaProperties;
    @Override
    public NasaFeedResponse getFeed(LocalDate startDate, LocalDate endDate) {
        return nasaWebClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/neo/rest/v1/feed")
                                .queryParam("start_date", startDate)
                                .queryParam("end_date", endDate)
                                .queryParam("api_key", nasaProperties.getApiKey())
                                .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body ->
                                        reactor.core.publisher.Mono.error(
                                                new NasaApiException(
                                                        "NASA API returned error: "
                                                                + response.statusCode()
                                                )
                                        )
                                )
                )
                .bodyToMono(NasaFeedResponse.class)
                .block();
    }

    @Override
    public NasaLookupResponse getLookup(String asteroidId) {
        return nasaWebClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/neo/rest/v1/neo/{id}")
                                .queryParam(
                                        "api_key",
                                        nasaProperties.getApiKey()
                                )
                                .build(asteroidId)
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response ->
                                response.bodyToMono(String.class)
                                        .flatMap(body ->
                                                reactor.core.publisher.Mono.error(
                                                        new NasaApiException(
                                                                "NASA Lookup API returned error: "
                                                                        + response.statusCode()
                                                                        + ", body: "
                                                                        + body
                                                        )
                                                )
                                        )
                )
                .bodyToMono(NasaLookupResponse.class)
                .block();
    }
}
