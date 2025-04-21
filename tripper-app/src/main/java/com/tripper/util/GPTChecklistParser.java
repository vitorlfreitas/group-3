package com.tripper.util;

import com.tripper.model.TripChecklistSection;

import java.util.ArrayList;
import java.util.List;

public class GPTChecklistParser {

    /**
     * Splits the GPT response into city-based sections by looking for lines like: **CITY NAME**
     * Then for each city block, we parse headings:
     * **Weather:**, **Clothing:**, **Accessories:**, **Optional Items:**, line by line.
     */
    public static List<TripChecklistSection> parse(String gptText) {
        List<TripChecklistSection> sections = new ArrayList<>();

        // Split the entire GPT text by lines
        String[] lines = gptText.split("\\r?\\n");

        String currentCityName = null;
        List<String> cityLines = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            // 1) Detect city heading: e.g. **RIO DE JANEIRO, BRAZIL**
            if (line.matches("\\*\\*[A-Z0-9 ,.'()-]+\\*\\*")) {
                // If there's a previous city block, parse it
                if (currentCityName != null && !cityLines.isEmpty()) {
                    sections.add(parseCityBlock(currentCityName, cityLines));
                }
                // Start new city block
                currentCityName = line.replace("**", "").trim();
                cityLines.clear();
            }
            // If it's not a city heading, accumulate lines for the current city
            else if (currentCityName != null) {
                cityLines.add(line);
            }
        }

        // If there's one last city block to parse
        if (currentCityName != null && !cityLines.isEmpty()) {
            sections.add(parseCityBlock(currentCityName, cityLines));
        }

        return sections;
    }

    /**
     * Parses the lines for a single city. We look for headings like:
     * **Weather:**, **Clothing:**, **Accessories:**, **Optional Items:**
     * Then store bullet lines under the correct heading.
     */
    private static TripChecklistSection parseCityBlock(String cityName, List<String> lines) {
        TripChecklistSection section = new TripChecklistSection();
        section.setCityName(cityName);

        String currentHeading = "";
        List<String> buffer = new ArrayList<>();

        for (String line : lines) {
            // Check if line is a heading, e.g. **Weather:**
            if (line.equalsIgnoreCase("**Weather:**")) {
                // store the previous heading's buffer
                storeBufferInSection(section, currentHeading, buffer);
                currentHeading = "weather";
                buffer.clear();
            }
            else if (line.equalsIgnoreCase("**Clothing:**")) {
                storeBufferInSection(section, currentHeading, buffer);
                currentHeading = "clothing";
                buffer.clear();
            }
            else if (line.equalsIgnoreCase("**Accessories:**")) {
                storeBufferInSection(section, currentHeading, buffer);
                currentHeading = "accessories";
                buffer.clear();
            }
            else if (line.equalsIgnoreCase("**Optional Items:**")) {
                storeBufferInSection(section, currentHeading, buffer);
                currentHeading = "optional";
                buffer.clear();
            }
            else {
                // It's either a bullet line or plain text under the current heading
                buffer.add(line);
            }
        }

        // Flush the last heading data
        storeBufferInSection(section, currentHeading, buffer);

        return section;
    }

    /**
     * Stores the buffered lines into the TripChecklistSection depending on the current heading.
     * For bullet lines (starting with '-' or '•'), we parse them into a list. For Weather, we just combine text.
     */
    private static void storeBufferInSection(TripChecklistSection section, String heading, List<String> buffer) {
        if (heading.isEmpty() || buffer.isEmpty()) {
            return; // no heading or no content
        }

        // Collect bullet lines (e.g. "- item", "• item")
        List<String> bulletItems = new ArrayList<>();
        List<String> textLines = new ArrayList<>();

        for (String line : buffer) {
            if (line.startsWith("-") || line.startsWith("•")) {
                // Remove bullet char
                line = line.replaceFirst("[-•]\\s*", "").trim();
                bulletItems.add(line);
            } else {
                // plain text line
                textLines.add(line);
            }
        }

        switch (heading.toLowerCase()) {
            case "weather" -> {
                // If there's bullet lines, combine them with the text lines
                // Often you'll have text describing the weather and bullet lines
                String combined = String.join(" ", textLines);
                if (!bulletItems.isEmpty()) {
                    combined += (combined.isEmpty() ? "" : " ") + String.join(". ", bulletItems);
                }
                section.setWeather(combined.trim());
            }
            case "clothing" -> section.setClothing(bulletItems);
            case "accessories" -> section.setAccessories(bulletItems);
            case "optional" -> section.setOptionalItems(bulletItems);
            default -> {
                // Ignore
            }
        }
    }
}
