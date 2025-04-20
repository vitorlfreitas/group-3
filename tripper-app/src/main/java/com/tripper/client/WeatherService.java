package com.tripper.client;

import com.tripper.config.OpenWeatherProperties;
import com.tripper.model.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Service to fetch weather data from OpenWeather API.
 * It uses WebClient to make HTTP requests and handle responses.
 * The service is configured with the base URL and API key from application
 * properties.
 * It provides a method to get the weather forecast for a given location.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Component
public class WeatherService {

    private final WebClient webClient;
    private final OpenWeatherProperties props;

    // Constructor to initialize WebClient with base URL and API key
    public WeatherService(WebClient.Builder webClientBuilder, OpenWeatherProperties props) {
        this.props = props;
        this.webClient = webClientBuilder.baseUrl(props.getUrl()).build();
    }

    // Method to fetch weather forecast data for a specific location
    public WeatherResponse getForecastData(String location) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", location)
                            .queryParam("appid", props.getKey())
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .block();

        }
        // Handle specific exceptions for better error handling
        catch (WebClientResponseException e) {

            System.err.println("API error: " + e.getStatusCode() + e.getMessage());

        }
        // Handle other exceptions
        catch (Exception e) {

            System.err.println("Error fetching forecast for location: " + location);
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
}
