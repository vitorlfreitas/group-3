package com.tripper.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TripRequest {
    private String userName;
    private String tripDetails;
    private String location;
    private boolean generatePdf;


}
