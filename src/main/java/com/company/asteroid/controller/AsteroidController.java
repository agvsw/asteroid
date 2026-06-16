package com.company.asteroid.controller;

import com.company.asteroid.dto.response.AsteroidListResponse;
import com.company.asteroid.service.AsteroidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/asteroids")
@RequiredArgsConstructor
@Validated
public class AsteroidController {

    private final AsteroidService asteroidService;

    @GetMapping
    public ResponseEntity<AsteroidListResponse> getClosestAsteroids(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(
                asteroidService.getClosestAsteroids(
                        startDate,
                        endDate
                )
        );
    }
}
