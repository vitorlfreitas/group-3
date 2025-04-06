package com.tripper.client;

import com.google.gson.Gson;
import com.tripper.model.WeatherResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class WeatherService {

    // PLEASE DO NOT SHARE YOUR API KEY publicly.
    private static final String API_KEY = "your-openweather-api-key";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast";

    public WeatherResponse getForecastData(String location) {
        try {
            String urlString = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, location, API_KEY);
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    // Parse JSON using Gson
                    Gson gson = new Gson();
                    return gson.fromJson(response.toString(), WeatherResponse.class);
                }
            } else {
                System.out.println("Error fetching weather data. HTTP code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }
}
