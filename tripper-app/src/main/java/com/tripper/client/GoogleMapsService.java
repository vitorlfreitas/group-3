package com.tripper.client;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapsService {

    private final GeoApiContext context;

    public GoogleMapsService(@Value("${google.api.key}") String apiKey) {
        this.context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

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
