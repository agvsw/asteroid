package com.company.asteroid.service;

import com.company.asteroid.dto.response.AsteroidListResponse;

import java.time.LocalDate;

public interface AsteroidService {
    AsteroidListResponse getClosestAsteroids(LocalDate startDate, LocalDate endDate);
}
