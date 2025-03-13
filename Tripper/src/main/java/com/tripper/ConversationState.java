package main.java.com.tripper;

import java.util.ArrayList;
import java.util.List;

public class ConversationState {
    private String userName;
    private List<String> locations;
    private String tripDetails;
    private boolean detailsConfirmed;

    public ConversationState() {
        locations = new ArrayList<>();
        detailsConfirmed = false;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public List<String> getLocations() {
        return locations;
    }
    public void addLocation(String location) {
        locations.add(location);
    }
    public String getTripDetails() {
        return tripDetails;
    }
    public void setTripDetails(String tripDetails) {
        this.tripDetails = tripDetails;
    }
    public boolean isDetailsConfirmed() {
        return detailsConfirmed;
    }
    public void setDetailsConfirmed(boolean detailsConfirmed) {
        this.detailsConfirmed = detailsConfirmed;
    }
}
