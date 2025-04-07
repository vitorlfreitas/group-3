package com.tripper.client;

import com.google.gson.Gson;
import com.tripper.model.WeatherResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class WeatherService {

    private static final String API_KEY = "3546933fd7ea84c81dd36f6faa3dbe9d";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast";

    public WeatherResponse getForecastData(String location) {
        try {
            String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());

            String urlString = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, encodedLocation, API_KEY);
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    return new Gson().fromJson(responseBuilder.toString(), WeatherResponse.class);
                }
            } else {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorBuilder.append(line);
                    }
                    System.err.printf("Error fetching weather data for '%s'. HTTP %d - %s%n",
                            location, responseCode, errorBuilder);
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encoding error for location: " + location);
        } catch (Exception e) {
            System.err.println("Exception while calling OpenWeatherMap API: " + e.getMessage());
        }
        return null;
    }
}
