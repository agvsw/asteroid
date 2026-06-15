package com.company.asteroid.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiameterMeters {
    @JsonProperty("estimated_diameter_min")
    private BigDecimal min;

    @JsonProperty("estimated_diameter_max")
    private BigDecimal max;
}
