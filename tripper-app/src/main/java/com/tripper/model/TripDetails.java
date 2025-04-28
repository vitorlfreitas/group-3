package com.tripper.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TripDetails {
    private List<String> locations;
    private String travelMonth;

    @Override
    public String toString() {
        return "TripDetails [locations=" + locations + ", travelMonth=" + travelMonth + "]";
    }
}
