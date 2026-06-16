package com.company.asteroid.mapper;

import com.company.asteroid.dto.nasa.*;
import com.company.asteroid.dto.response.AsteroidResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AsteroidMapperTest {
    private final AsteroidMapper mapper =
            Mappers.getMapper(AsteroidMapper.class);

    @Test
    void shouldMapAsteroidResponse() {

        MissDistance missDistance =
                new MissDistance();

        missDistance.setKilometers(
                BigDecimal.valueOf(12345.67)
        );

        CloseApproachData approach =
                new CloseApproachData();

        approach.setCloseApproachDate(
                "2026-06-10"
        );

        approach.setMissDistance(
                missDistance
        );

        DiameterMeters meters =
                new DiameterMeters();

        meters.setMin(
                BigDecimal.valueOf(123.45)
        );

        EstimatedDiameter diameter =
                new EstimatedDiameter();

        diameter.setMeters(
                meters
        );

        NasaNearEarthObject asteroid =
                new NasaNearEarthObject();

        asteroid.setId("123");
        asteroid.setName("Apollo");
        asteroid.setPotentiallyHazardous(true);
        asteroid.setEstimatedDiameter(diameter);
        asteroid.setCloseApproachData(
                List.of(approach)
        );

        AsteroidResponse result =
                mapper.toResponse(asteroid);

        assertNotNull(result);

        assertEquals(
                "123",
                result.id()
        );

        assertEquals(
                "Apollo",
                result.name()
        );

        assertEquals(
                "2026-06-10",
                result.closestApproachDate()
        );

        assertEquals(
                BigDecimal.valueOf(12345.67),
                result.missDistanceKm()
        );

        assertEquals(
                BigDecimal.valueOf(123.45),
                result.estimatedDiameterMeters()
        );

        assertTrue(
                result.potentiallyHazardous()
        );
    }

    @Test
    void shouldReturnNullDiameterWhenDiameterMissing() {

        NasaNearEarthObject asteroid =
                new NasaNearEarthObject();

        asteroid.setId("123");
        asteroid.setName("Apollo");
        asteroid.setEstimatedDiameter(null);
        asteroid.setCloseApproachData(List.of());

        AsteroidResponse result =
                mapper.toResponse(asteroid);

        assertNull(
                result.estimatedDiameterMeters()
        );
    }

    @Test
    void shouldReturnNullApproachFieldsWhenNoApproachData() {

        NasaNearEarthObject asteroid =
                new NasaNearEarthObject();

        asteroid.setId("123");
        asteroid.setName("Apollo");
        asteroid.setCloseApproachData(List.of());

        AsteroidResponse result =
                mapper.toResponse(asteroid);

        assertNull(
                result.closestApproachDate()
        );

        assertNull(
                result.missDistanceKm()
        );
    }
}
