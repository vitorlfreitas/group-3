package main.java.com.tripper;

public class TripChecklistGenerator {

    /**
     * Generate checklist arrays based on trip details.
     */
    public static TripChecklist generateChecklist(TripDetails details) {
        String travelMonth = details.getTravelMonth();
        if (travelMonth == null) {
            travelMonth = "";
        }
        travelMonth = travelMonth.toLowerCase();

        String[] essentialItems;
        String[] recommendations;
        String[] optionalItems;
        String[] accessories;

        // Simple heuristics based on travel month
        if (travelMonth.contains("june") || travelMonth.contains("july") || travelMonth.contains("august") || travelMonth.contains("summer")) {
            essentialItems = new String[] {"Light T-Shirt", "Shorts", "Comfortable Walking Shoes", "Sunglasses"};
            recommendations = new String[] {"Hat", "Sunscreen", "Umbrella (in case of rain)"};
            optionalItems = new String[] {"Light Sweater", "Extra Pair of Socks"};
            accessories = new String[] {"Crossbody Bag", "Travel Adapter", "Power Bank"};
        } else if (travelMonth.contains("december") || travelMonth.contains("january") || travelMonth.contains("february") || travelMonth.contains("winter")) {
            essentialItems = new String[] {"Warm Jacket", "Thermal Wear", "Gloves", "Scarf", "Beanie"};
            recommendations = new String[] {"Boots", "Extra Socks"};
            optionalItems = new String[] {"Lip Balm", "Hand Warmers"};
            accessories = new String[] {"Backpack", "Travel Adapter"};
        } else {
            // Neutral list if month isn't clear
            essentialItems = new String[] {"Versatile T-Shirt", "Jeans", "Comfortable Shoes"};
            recommendations = new String[] {"Light Jacket", "Umbrella"};
            optionalItems = new String[] {"Hat", "Sunglasses"};
            accessories = new String[] {"Backpack", "Portable Charger"};
        }
        return new TripChecklist(essentialItems, recommendations, optionalItems, accessories);
    }
}

