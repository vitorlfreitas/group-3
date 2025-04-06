package com.tripper.model;

public class TripResponse {
    private String userName;
    private String recommendations;   // ChatGPT dynamic response
    private String[] essentialItems;
    private String[] recommendationsList;
    private String[] optionalItems;
    private String[] accessories;
    private String pdfFileName;       // If a PDF checklist was generated

    // Getters and setters

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getRecommendations() {
        return recommendations;
    }
    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }
    public String[] getEssentialItems() {
        return essentialItems;
    }
    public void setEssentialItems(String[] essentialItems) {
        this.essentialItems = essentialItems;
    }
    public String[] getRecommendationsList() {
        return recommendationsList;
    }
    public void setRecommendationsList(String[] recommendationsList) {
        this.recommendationsList = recommendationsList;
    }
    public String[] getOptionalItems() {
        return optionalItems;
    }
    public void setOptionalItems(String[] optionalItems) {
        this.optionalItems = optionalItems;
    }
    public String[] getAccessories() {
        return accessories;
    }
    public void setAccessories(String[] accessories) {
        this.accessories = accessories;
    }
    public String getPdfFileName() {
        return pdfFileName;
    }
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }
}
