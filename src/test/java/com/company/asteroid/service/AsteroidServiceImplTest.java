package com.company.asteroid.service;

import com.company.asteroid.client.NasaClient;
import com.company.asteroid.dto.nasa.NasaFeedResponse;
import com.company.asteroid.dto.response.AsteroidListResponse;
import com.company.asteroid.dto.response.AsteroidResponse;
import com.company.asteroid.exception.InvalidDateRangeException;
import com.company.asteroid.processor.AsteroidProcessor;
import com.company.asteroid.validation.DateRangeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AsteroidServiceImplTest {

    @Mock
    private DateRangeValidator dateRangeValidator;

    @Mock
    private NasaClient nasaClient;

    @Mock
    private AsteroidProcessor asteroidProcessor;

    @InjectMocks
    private AsteroidServiceImpl asteroidService;

    @Test
    void shouldReturnClosestAsteroids() {

        LocalDate startDate = LocalDate.of(2026, 6, 9);
        LocalDate endDate = LocalDate.of(2026, 6, 11);

        NasaFeedResponse feedResponse = new NasaFeedResponse();

        AsteroidResponse asteroid =
                AsteroidResponse.builder()
                        .id("123")
                        .name("Apollo")
                        .build();

        when(
                nasaClient.getFeed(
                        startDate,
                        endDate
                )
        ).thenReturn(feedResponse);

        when(
                asteroidProcessor.findTopClosestAsteroids(
                        feedResponse
                )
        ).thenReturn(List.of(asteroid));

        AsteroidListResponse result =
                asteroidService.getClosestAsteroids(
                        startDate,
                        endDate
                );

        assertNotNull(result);

        assertEquals(
                startDate,
                result.startDate()
        );

        assertEquals(
                endDate,
                result.endDate()
        );

        assertEquals(
                1,
                result.count()
        );

        assertEquals(
                "123",
                result.asteroids()
                        .getFirst()
                        .id()
        );

        verify(dateRangeValidator)
                .validate(
                        startDate,
                        endDate
                );

        verify(nasaClient)
                .getFeed(
                        startDate,
                        endDate
                );

        verify(asteroidProcessor)
                .findTopClosestAsteroids(
                        feedResponse
                );
    }

    @Test
    void shouldPropagateValidationException() {

        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 15);

        doThrow(
                new InvalidDateRangeException(
                        "Date range must not exceed 7 days"
                )
        )
                .when(dateRangeValidator)
                .validate(
                        startDate,
                        endDate
                );

        assertThrows(
                InvalidDateRangeException.class,
                () -> asteroidService.getClosestAsteroids(
                        startDate,
                        endDate
                )
        );

        verifyNoInteractions(
                nasaClient,
                asteroidProcessor
        );
    }
}
