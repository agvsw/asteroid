package com.company.asteroid.processor;

import com.company.asteroid.dto.nasa.CloseApproachData;
import com.company.asteroid.dto.nasa.NasaFeedResponse;
import com.company.asteroid.dto.nasa.NasaNearEarthObject;
import com.company.asteroid.dto.response.AsteroidResponse;
import com.company.asteroid.mapper.AsteroidMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AsteroidProcessor {

    private static final int TOP_ASTEROIDS = 10;

    private final AsteroidMapper asteroidMapper;
    public List<AsteroidResponse> findTopClosestAsteroids(
            NasaFeedResponse feedResponse
    ) {

        if (feedResponse == null
                || feedResponse.getNearEarthObjects() == null) {
            return Collections.emptyList();
        }

        return feedResponse.getNearEarthObjects()
                .values()
                .stream()
                .flatMap(List::stream)
                .map(asteroidMapper::toResponse)
                .sorted(
                        Comparator.comparing(
                                AsteroidResponse::missDistanceKm
                        )
                )
                .limit(TOP_ASTEROIDS)
                .toList();
    }
}
