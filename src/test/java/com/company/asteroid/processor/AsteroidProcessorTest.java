package com.company.asteroid.processor;

import com.company.asteroid.dto.nasa.NasaFeedResponse;
import com.company.asteroid.dto.nasa.NasaNearEarthObject;
import com.company.asteroid.dto.response.AsteroidResponse;
import com.company.asteroid.mapper.AsteroidMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AsteroidProcessorTest {

    @Mock
    private AsteroidMapper asteroidMapper;

    @InjectMocks
    private AsteroidProcessor asteroidProcessor;

    @Test
    void shouldReturnEmptyListWhenFeedResponseIsNull() {

        List<AsteroidResponse> result =
                asteroidProcessor.findTopClosestAsteroids(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNearEarthObjectsIsNull() {

        NasaFeedResponse response = new NasaFeedResponse();

        List<AsteroidResponse> result =
                asteroidProcessor.findTopClosestAsteroids(response);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTop10ClosestAsteroidsSortedByMissDistance() {

        NasaFeedResponse response = new NasaFeedResponse();

        Map<String, List<NasaNearEarthObject>> nearEarthObjects =
                new HashMap<>();

        List<NasaNearEarthObject> asteroids =
                java.util.stream.IntStream.rangeClosed(1, 15)
                        .mapToObj(i -> new NasaNearEarthObject())
                        .toList();

        nearEarthObjects.put(
                "2026-06-10",
                asteroids
        );

        response.setNearEarthObjects(
                nearEarthObjects
        );

        for (int i = 0; i < asteroids.size(); i++) {

            int distance = 15 - i;

            AsteroidResponse mapped =
                    AsteroidResponse.builder()
                            .id(String.valueOf(i))
                            .missDistanceKm(
                                    BigDecimal.valueOf(distance)
                            )
                            .build();

            when(
                    asteroidMapper.toResponse(
                            same(asteroids.get(i))
                    )
            ).thenReturn(mapped);
        }

        List<AsteroidResponse> result =
                asteroidProcessor.findTopClosestAsteroids(
                        response
                );

        assertEquals(
                10,
                result.size()
        );

        assertEquals(
                BigDecimal.ONE,
                result.get(0).missDistanceKm()
        );

        assertEquals(
                BigDecimal.TEN,
                result.get(9).missDistanceKm()
        );

        verify(
                asteroidMapper,
                times(15)
        ).toResponse(any());
    }
}
