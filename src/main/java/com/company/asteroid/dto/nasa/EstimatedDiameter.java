package com.company.asteroid.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EstimatedDiameter {
    @JsonProperty("meters")
    private DiameterMeters meters;
}
