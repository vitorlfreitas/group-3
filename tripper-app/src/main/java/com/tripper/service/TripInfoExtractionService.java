package com.tripper.service;

import com.tripper.client.GoogleMapsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TripInfoExtractionService {

    private final GoogleMapsService googleMapsService;

    private final List<String> MONTHS = Arrays.asList(
            "january", "february", "march", "april", "may", "june",
            "july", "august", "september", "october", "november", "december"
    );

    private final List<String> DATE_PHRASES = Arrays.asList(
            "next week", "this week", "next month", "this month", "this summer", "next summer"
    );

    private final Pattern DATE_PATTERN = Pattern.compile(
            "(?i)(next week|this week|\\d{1,2}/\\d{1,2}|\\b(?:january|february|march|april|may|june|july|august|september|october|november|december)\\b)"
    );

    public Map<String, Object> extract(String text) {
        List<String> locations = new ArrayList<>();
        Set<String> dates = new HashSet<>();

        // Normalize and split text
        String[] words = text.split("\\s+");
        Set<String> stopWords = Set.of("to", "in", "at", "on", "from", "the");
        Set<String> verbStarters = Set.of("am", "is", "are", "was", "were", "have", "had", "planning");

        // Try sliding window (3 words, 2 words, 1 word)
        for (int i = 0; i < words.length; i++) {
            for (int window = 3; window >= 1; window--) {
                if (i + window > words.length) continue;

                String rawCandidate = String.join(" ", Arrays.copyOfRange(words, i, i + window));
                String cleaned = rawCandidate.replaceAll("[^a-zA-Z\\s]", "").trim(); // remove punctuation

                // Remove leading stop words
                String[] cleanedWords = cleaned.split("\\s+");
                if (cleanedWords.length == 0) continue;


                int skip = 0;
                while (skip < cleanedWords.length && stopWords.contains(cleanedWords[skip].toLowerCase())) {
                    skip++;
                }

                if (skip >= cleanedWords.length) continue;

                String finalCandidate = String.join(" ", Arrays.copyOfRange(cleanedWords, skip, cleanedWords.length));

                String[] candidateWords = finalCandidate.split("\\s+");

                if (verbStarters.contains(candidateWords[0].toLowerCase())) continue;

                if (
                        finalCandidate.length() < 3 ||
                                locations.contains(finalCandidate) ||
                                stopWords.contains(candidateWords[candidateWords.length - 1].toLowerCase())
                ) continue;

                if (googleMapsService.isValidLocation(finalCandidate)) {
                    locations.add(finalCandidate);
                }
            }
        }

        // Dates: keywords and regex
        String lower = text.toLowerCase();
        for (String phrase : DATE_PHRASES) {
            if (lower.contains(phrase)) dates.add(phrase);
        }

        for (String word : words) {
            if (MONTHS.contains(word.toLowerCase()) || DATE_PATTERN.matcher(word).find()) {
                dates.add(word);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("locations", locations);
        result.put("dates", new ArrayList<>(dates));
        return result;
    }

}
