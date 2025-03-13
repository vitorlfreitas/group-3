package main.java.com.tripper;

// Imports
import main.java.com.tripper.model.WeatherResponse;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/* I was using this API, but know only the ChatGPT. I am going to see what to do with this code :) */
public class WeatherService {

    /* PLEASE DO NOT SHARE MY API KEY, I TRUST YOU GUYS */
    private static final String API_KEY = "3546933fd7ea84c81dd36f6faa3dbe9d";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast";

    /**
     * Fetches weather forecast data from the OpenWeatherMap API
     * @param location The location to get the forecast for
     * @return WeatherResponse object containing the forecast data
     */
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
                    WeatherResponse weatherResponse = gson.fromJson(response.toString(), WeatherResponse.class);

                    return weatherResponse;
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
