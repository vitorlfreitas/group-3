package com.tripper.model;

import java.util.List;

public class TripChecklistSection {
    private String cityName;
    private String weather;
    private List<String> clothing;
    private List<String> accessories;
    private List<String> optionalItems;

    // Constructors
    public TripChecklistSection() {}

    public TripChecklistSection(String cityName, String weather, List<String> clothing, List<String> accessories, List<String> optionalItems) {
        this.cityName = cityName;
        this.weather = weather;
        this.clothing = clothing;
        this.accessories = accessories;
        this.optionalItems = optionalItems;
    }

    // Getters and setters
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public List<String> getClothing() { return clothing; }
    public void setClothing(List<String> clothing) { this.clothing = clothing; }

    public List<String> getAccessories() { return accessories; }
    public void setAccessories(List<String> accessories) { this.accessories = accessories; }

    public List<String> getOptionalItems() { return optionalItems; }
    public void setOptionalItems(List<String> optionalItems) { this.optionalItems = optionalItems; }
}
