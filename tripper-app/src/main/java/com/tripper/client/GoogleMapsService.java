package com.tripper.client;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * GoogleMapsService is a service class that interacts with the Google Maps Geocoding API.
 * It validates locations by checking if a given place name corresponds to a valid geographical location.
 * The API key is injected from application properties.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Service
public class GoogleMapsService {

    private final GeoApiContext context;

    // Constructor that initializes the GeoApiContext with the provided API key
    public GoogleMapsService(@Value("${google.api.key}") String apiKey) {
        this.context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    // Method to validate if a given place name corresponds to a valid location
    public boolean isValidLocation(String place) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, place).await();
            return results.length > 0;
        } catch (Exception e) {
            System.err.println("Geocoding error: " + e.getMessage());
            return false;
        }
    }
}
