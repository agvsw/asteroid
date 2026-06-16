package com.company.asteroid.controller;

import com.company.asteroid.dto.response.AsteroidListResponse;
import com.company.asteroid.exception.InvalidDateRangeException;
import com.company.asteroid.service.AsteroidService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsteroidController.class)
public class AsteroidControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AsteroidService asteroidService;

    @Test
    void shouldReturnClosestAsteroids() throws Exception {

        AsteroidListResponse response =
                AsteroidListResponse.builder()
                        .startDate(LocalDate.of(2026, 6, 9))
                        .endDate(LocalDate.of(2026, 6, 11))
                        .count(0)
                        .asteroids(Collections.emptyList())
                        .build();

        when(
                asteroidService.getClosestAsteroids(
                        any(),
                        any()
                )
        ).thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/asteroids")
                                .param("startDate", "2026-06-09")
                                .param("endDate", "2026-06-11")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.count")
                                .value(0)
                )
                .andExpect(
                        jsonPath("$.startDate")
                                .value("2026-06-09")
                )
                .andExpect(
                        jsonPath("$.endDate")
                                .value("2026-06-11")
                );
    }

    @Test
    void shouldReturnBadRequestWhenDateFormatInvalid() throws Exception {

        mockMvc.perform(
                        get("/api/v1/asteroids")
                                .param("startDate", "09-06-2026")
                                .param("endDate", "2026-06-11")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message")
                                .value("Invalid date format. Expected format: YYYY-MM-DD")
                );
    }

    @Test
    void shouldReturnBadRequestWhenDateRangeInvalid() throws Exception {

        when(
                asteroidService.getClosestAsteroids(
                        any(),
                        any()
                )
        ).thenThrow(
                new InvalidDateRangeException(
                        "Date range must not exceed 7 days"
                )
        );

        mockMvc.perform(
                        get("/api/v1/asteroids")
                                .param("startDate", "2026-06-01")
                                .param("endDate", "2026-06-15")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message")
                                .value("Date range must not exceed 7 days")
                );
    }
}
