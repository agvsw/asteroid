package com.company.asteroid.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NasaLookupResponse {
    private String id;

    private String name;

    @JsonProperty("absolute_magnitude_h")
    private Double absoluteMagnitude;

    @JsonProperty("estimated_diameter")
    private EstimatedDiameter estimatedDiameter;

    @JsonProperty("is_potentially_hazardous_asteroid")
    private Boolean potentiallyHazardous;

    @JsonProperty("close_approach_data")
    private List<CloseApproachData> closeApproachData;
}
