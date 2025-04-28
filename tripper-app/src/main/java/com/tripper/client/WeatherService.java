package com.tripper.client;

import com.tripper.config.OpenWeatherProperties;
import com.tripper.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;

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
@Service
@Slf4j
public class WeatherService {

    private final WebClient webClient;
    private final OpenWeatherProperties props;

    // Constructor to initialize WebClient with base URL and API key
    public WeatherService(WebClient.Builder webClientBuilder, OpenWeatherProperties props) {
        this.props = props;
        this.webClient = webClientBuilder.baseUrl(props.getUrl()).build();
    }

    /**
     * Fetches weather forecast data for a given location.
     * The method is annotated with @Cacheable to cache the result
     * based on the location parameter. The cache key is the location
     * in lowercase, and the result is cached unless it is empty.
     *
     * @param location the location for which to fetch weather data
     * @return an Optional containing the WeatherResponse if successful, or an empty Optional if an error occurs
     * @throws WebClientResponseException if the API returns an error response
     * 
     * @see WeatherResponse
     * @see OpenWeatherProperties
     * @see WebClient
     * @see <a href="https://openweathermap.org/api">OpenWeather API</a>
     * 
     * @author vitorlfreitas
     */
    @Cacheable(value = "forecastCache", unless = "#result == null || #result.list == null || #result.list.isEmpty()")

    public Optional<WeatherResponse> getForecastData(String location) {
        try {
            return Optional.ofNullable(
                webClient.get()
                    .uri(uriBuilder -> uriBuilder
                        .queryParam("q", location)
                        .queryParam("appid", props.getKey())
                        .queryParam("units", "metric")
                        .build())
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)))
                    .block()
            );

        }
        catch (WebClientResponseException e) {
            log.warn("OpenWeather API {} error for {}: {}",
                e.getStatusCode(), location, e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Failed to fetch weather for {}: {}", location, e.getMessage(), e);
        }
        return Optional.empty();
    }
}
