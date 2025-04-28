package com.tripper.dto;

import java.util.List;

/**
 * TripInfo is a record that holds information about a trip.
 * It contains two lists: one for locations and another for dates.
 * This class is used to represent the details of a trip in a structured way.
 *
 * @param locations A list of strings representing the locations of the trip.
 * @param dates A list of strings representing the dates associated with the trip.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 */
public record TripInfo(
        List<String> locations,
        List<String> dates
) {}
