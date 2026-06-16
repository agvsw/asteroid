package com.company.asteroid.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MissDistance {
    @JsonProperty("kilometers")
    private BigDecimal kilometers;
}
