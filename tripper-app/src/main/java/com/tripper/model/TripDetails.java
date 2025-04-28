package com.tripper.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * TripDetails is a class that represents the details of a trip.
 * It contains information about the locations and the months of travel.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 */
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
