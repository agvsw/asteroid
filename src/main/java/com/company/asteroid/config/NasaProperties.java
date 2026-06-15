package com.company.asteroid.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "nasa.api")
public class NasaProperties {
    private String baseUrl;

    private String apiKey;

}
