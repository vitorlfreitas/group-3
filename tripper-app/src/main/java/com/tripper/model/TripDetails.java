package com.tripper.model;

import java.util.List;

public class TripDetails {
    private List<String> locations;
    private String travelMonth;

    public List<String> getLocations() {
        return locations;
    }
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
    public String getTravelMonth() {
        return travelMonth;
    }
    public void setTravelMonth(String travelMonth) {
        this.travelMonth = travelMonth;
    }

    @Override
    public String toString() {
        return "TripDetails [locations=" + locations + ", travelMonth=" + travelMonth + "]";
    }
}
