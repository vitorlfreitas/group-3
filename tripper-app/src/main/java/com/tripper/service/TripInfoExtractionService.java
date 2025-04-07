package com.tripper.service;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class TripInfoExtractionService {

    private final POSTaggerME posTagger;
    private final SimpleTokenizer tokenizer;

    private final List<String> MONTHS = Arrays.asList(
            "january", "february", "march", "april", "may", "june",
            "july", "august", "september", "october", "november", "december"
    );

    private final Pattern DATE_PATTERN = Pattern.compile("(?i)(next week|this week|\\d{1,2}/\\d{1,2}|\\b(?:january|february|march|april|may|june|july|august|september|october|november|december)\\b)");

    private final List<String> DATE_PHRASES = Arrays.asList(
            "next week", "this week", "next month", "this month", "this summer", "next summer"
    );


    public TripInfoExtractionService() {
        try {
            InputStream modelIn = new ClassPathResource("models/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin").getInputStream();
            POSModel model = new POSModel(modelIn);
            this.posTagger = new POSTaggerME(model);
            this.tokenizer = SimpleTokenizer.INSTANCE;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load POS model", e);
        }
    }

    public Map<String, Object> extract(String text) {
        List<String> locations = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        String[] tokens = tokenizer.tokenize(text);

        // Basic capitalized location matcher (could be improved with actual list)
        for (String token : tokens) {
            if (Character.isUpperCase(token.charAt(0))) {
                // Naive location filter to avoid picking names like "I", "I'm", etc.
                if (!token.equals("I") && !token.equals("I'm") && token.length() > 2) {
                    locations.add(token);
                }
            }

            // Date regex (e.g., months, "next week", etc.)
            if (MONTHS.contains(token.toLowerCase()) || DATE_PATTERN.matcher(token).find()) {
                dates.add(token);
            }
        }

        Map<String, Object> result = new HashMap<>();

        // Scan the full sentence for date phrases
        String lowerText = text.toLowerCase();
        for (String phrase : DATE_PHRASES) {
            if (lowerText.contains(phrase)) {
                dates.add(phrase);
            }
        }

        // Remove overlap (e.g., "August" showing as both)
        locations.removeIf(dates::contains);

        result.put("locations", locations);
        result.put("dates", dates);

        return result;
    }


}
