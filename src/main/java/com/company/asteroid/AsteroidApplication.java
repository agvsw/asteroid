package com.company.asteroid;

import com.company.asteroid.config.NasaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NasaProperties.class)
public class AsteroidApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsteroidApplication.class, args);
	}

}
