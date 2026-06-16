package com.company.asteroid.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AsteroidListResponse(
        LocalDate startDate,
        LocalDate endDate,
        Integer count,
        List<AsteroidResponse> asteroids
) {
}
