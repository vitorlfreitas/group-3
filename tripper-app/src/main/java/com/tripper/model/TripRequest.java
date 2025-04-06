package com.tripper.model;

public class TripRequest {
    private String userName;
    private String tripDetails;
    private String location;   // e.g., "Paris" for weather info
    private boolean generatePdf;

    // Getters and setters

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getTripDetails() {
        return tripDetails;
    }
    public void setTripDetails(String tripDetails) {
        this.tripDetails = tripDetails;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public boolean isGeneratePdf() {
        return generatePdf;
    }
    public void setGeneratePdf(boolean generatePdf) {
        this.generatePdf = generatePdf;
    }
}
