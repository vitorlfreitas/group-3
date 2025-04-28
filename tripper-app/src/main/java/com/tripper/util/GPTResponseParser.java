package com.tripper.util;

import com.tripper.model.TripChecklistSection;

import java.util.Arrays;
import java.util.List;

/**
 * GPTResponseParser is a utility class that provides methods to parse and create
 * trip checklist sections based on example responses.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
public class GPTResponseParser {

    public static List<TripChecklistSection> parseFromExampleResponse() {
        TripChecklistSection paris = new TripChecklistSection(
                "Paris",
                "August in Paris is warm and generally sunny with occasional rain showers. Average temperatures range from 17°C (63°F) to 26°C (79°F).",
                Arrays.asList(
                        "Lightweight clothes such as t-shirts, shorts, and breathable fabrics",
                        "A light sweater or jacket for cooler evenings",
                        "Comfortable walking shoes for exploring the city",
                        "Umbrella or a lightweight rain jacket for unexpected showers"
                ),
                Arrays.asList(
                        "Stylish scarf",
                        "Sunglasses",
                        "Crossbody bag"
                ),
                Arrays.asList(
                        "Dressier outfit for a night out",
                        "Reusable water bottle"
                )
        );

        TripChecklistSection barcelona = new TripChecklistSection(
                "Barcelona",
                "August in Barcelona is hot and sunny, with temperatures ranging from 22°C (72°F) to 30°C (86°F).",
                Arrays.asList(
                        "Light and airy clothing like tank tops, shorts, and cotton dresses",
                        "Swimwear for lounging on the beaches",
                        "Comfortable sandals or sneakers",
                        "Light cardigan or wrap for cooler evenings"
                ),
                Arrays.asList(
                        "Sun hat",
                        "Beach towel",
                        "Small backpack"
                ),
                Arrays.asList(
                        "Beach accessories like a beach bag, flip-flops, and a cover-up",
                        "Guidebook or smartphone with a travel app"
                )
        );

        return List.of(paris, barcelona);
    }
}
