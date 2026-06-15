package com.company.asteroid.client;

import com.company.asteroid.dto.nasa.NasaFeedResponse;
import com.company.asteroid.dto.nasa.NasaLookupResponse;

import java.time.LocalDate;

public interface NasaClient {
    NasaFeedResponse getFeed(LocalDate startDate, LocalDate endDate);
    NasaLookupResponse getLookup(String asteroidId);
}
