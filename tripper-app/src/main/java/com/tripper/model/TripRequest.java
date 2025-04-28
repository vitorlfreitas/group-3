package com.tripper.model;

import lombok.Getter;
import lombok.Setter;

/**
 * TripRequest is a class that represents a request for trip information.
 * It contains information about the user, trip details, location, and whether
 * to generate a PDF.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Setter
@Getter
public class TripRequest {
    private String userName;
    private String tripDetails;
    private String location;
    private boolean generatePdf;


}
