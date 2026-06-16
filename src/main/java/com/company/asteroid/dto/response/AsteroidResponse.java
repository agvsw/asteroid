package com.company.asteroid.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public record AsteroidResponse(
        String id,
        String name,
        String closestApproachDate,
        BigDecimal missDistanceKm,
        BigDecimal estimatedDiameterMeters,
        Boolean potentiallyHazardous
) {
}
