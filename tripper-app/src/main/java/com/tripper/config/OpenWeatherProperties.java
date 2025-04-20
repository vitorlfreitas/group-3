package com.tripper.config; // Defines the package for the class

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter // Lombok annotation to generate all setter methods for the fields
@Getter // Lombok annotation to generate all getter methods for the fields
@Component // Marks this class as a Spring component so it can be scanned and managed by Spring
@ConfigurationProperties(prefix = "weather.api") // Binds properties with the prefix "weather.api" from configuration files
public class OpenWeatherProperties {

    private String key; // Stores the API key for the weather service
    private String url; // Stores the base URL for the weather service

}