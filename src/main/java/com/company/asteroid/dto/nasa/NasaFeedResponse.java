package com.company.asteroid.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NasaFeedResponse {

    @JsonProperty("element_count")
    private Integer elementCount;

    @JsonProperty("near_earth_objects")
    private Map<String, List<NasaNearEarthObject>> nearEarthObjects;
}
