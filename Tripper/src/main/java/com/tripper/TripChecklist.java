package main.java.com.tripper;

public class TripChecklist {
    private String[] essentialItems;
    private String[] recommendations;
    private String[] optionalItems;
    private String[] accessories;

    public TripChecklist(String[] essentialItems, String[] recommendations, String[] optionalItems, String[] accessories) {
        this.essentialItems = essentialItems;
        this.recommendations = recommendations;
        this.optionalItems = optionalItems;
        this.accessories = accessories;
    }

    public String[] getEssentialItems() {
        return essentialItems;
    }

    public String[] getRecommendations() {
        return recommendations;
    }

    public String[] getOptionalItems() {
        return optionalItems;
    }

    public String[] getAccessories() {
        return accessories;
    }
}
