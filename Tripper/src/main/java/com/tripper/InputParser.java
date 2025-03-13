package main.java.com.tripper;

import java.util.*;
import java.util.regex.*;

public class InputParser {
    // Regex to match any month name (case-insensitive)
    private static final Pattern monthPattern = Pattern.compile("(?i)\\b(January|February|March|April|May|June|July|August|September|October|November|December)\\b");

    public static TripDetails parseTripDetails(String input) {
        TripDetails details = new TripDetails();

        // Extract travel month using regex
        Matcher monthMatcher = monthPattern.matcher(input);
        if (monthMatcher.find()) {
            details.setTravelMonth(monthMatcher.group(1));
        }

        // Extract potential locations: split input into tokens and select capitalized words that aren't month names.
        String[] tokens = input.split("[,\\s]+");
        Set<String> monthSet = new HashSet<>(Arrays.asList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
        List<String> locations = new ArrayList<>();
        for (String token : tokens) {
            // Remove punctuation
            token = token.replaceAll("[^A-Za-z]", "");
            if (token.length() > 0 && Character.isUpperCase(token.charAt(0)) && !monthSet.contains(token)) {
                locations.add(token);
            }
        }
        details.setLocations(locations);
        return details;
    }
}
