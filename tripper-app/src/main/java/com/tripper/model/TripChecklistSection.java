package com.tripper.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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

}
