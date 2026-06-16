package com.company.asteroid.service;

import com.company.asteroid.client.NasaClient;
import com.company.asteroid.dto.nasa.NasaFeedResponse;
import com.company.asteroid.dto.response.AsteroidListResponse;
import com.company.asteroid.dto.response.AsteroidResponse;
import com.company.asteroid.processor.AsteroidProcessor;
import com.company.asteroid.validation.DateRangeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsteroidServiceImpl implements AsteroidService {

    private final DateRangeValidator dateRangeValidator;

    private final NasaClient nasaClient;

    private final AsteroidProcessor asteroidProcessor;

    @Override
    public AsteroidListResponse getClosestAsteroids(LocalDate startDate, LocalDate endDate) {
        dateRangeValidator.validate(
                startDate,
                endDate
        );

        NasaFeedResponse feedResponse = nasaClient.getFeed(
                startDate,
                endDate
        );

        List<AsteroidResponse> asteroids =
                asteroidProcessor.findTopClosestAsteroids(
                        feedResponse
                );

        return AsteroidListResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .count(asteroids.size())
                .asteroids(asteroids)
                .build();
    }
}
