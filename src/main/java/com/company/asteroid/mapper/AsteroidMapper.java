package com.company.asteroid.mapper;

import com.company.asteroid.dto.nasa.CloseApproachData;
import com.company.asteroid.dto.nasa.NasaNearEarthObject;
import com.company.asteroid.dto.response.AsteroidResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsteroidMapper {

    @Mapping(target = "closestApproachDate",
            expression = "java(extractApproachDate(asteroid))")
    @Mapping(target = "missDistanceKm",
            expression = "java(extractMissDistance(asteroid))")
    @Mapping(target = "estimatedDiameterMeters",
            expression = "java(extractDiameter(asteroid))")
    @Mapping(target = "potentiallyHazardous",
            source = "potentiallyHazardous")
    AsteroidResponse toResponse(
            NasaNearEarthObject asteroid
    );

    default String extractApproachDate(
            NasaNearEarthObject asteroid
    ) {

        CloseApproachData approach =
                asteroid.getCloseApproachData()
                        .stream()
                        .findFirst()
                        .orElse(null);

        return approach != null
                ? approach.getCloseApproachDate()
                : null;
    }

    default java.math.BigDecimal extractMissDistance(
            NasaNearEarthObject asteroid
    ) {

        CloseApproachData approach =
                asteroid.getCloseApproachData()
                        .stream()
                        .findFirst()
                        .orElse(null);

        return approach != null
                ? approach.getMissDistance()
                .getKilometers()
                : null;
    }

    default java.math.BigDecimal extractDiameter(
            NasaNearEarthObject asteroid
    ) {

        if (asteroid.getEstimatedDiameter() == null
                || asteroid.getEstimatedDiameter().getMeters() == null) {
            return null;
        }

        return asteroid.getEstimatedDiameter()
                .getMeters()
                .getMin();
    }
}
