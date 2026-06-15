package com.company.asteroid.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloseApproachData {

    @JsonProperty("close_approach_date")
    private String closeApproachDate;

    @JsonProperty("miss_distance")
    private MissDistance missDistance;
}
