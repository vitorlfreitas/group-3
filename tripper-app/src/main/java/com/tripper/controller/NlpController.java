package com.tripper.controller;

import com.tripper.service.SentenceDetectionService;
import com.tripper.service.TripInfoExtractionService;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/nlp")
public class NlpController {

    @PostMapping("/tokenize")
    public Map<String, Object> tokenize(@RequestBody Map<String, String> request) {
        String sentence = request.get("text");

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(sentence);

        Map<String, Object> response = new HashMap<>();
        response.put("original", sentence);
        response.put("tokens", tokens);

        return response;
    }

    @Autowired
    private SentenceDetectionService sentenceDetectionService;

    @PostMapping("/sentences")
    public Map<String, Object> detectSentences(@RequestBody Map<String, String> request) {
        String text = request.get("text");

        String[] sentences = sentenceDetectionService.detectSentences(text);

        Map<String, Object> response = new HashMap<>();
        response.put("original", text);
        response.put("sentences", sentences);

        return response;
    }

    @Autowired
    private TripInfoExtractionService tripInfoExtractionService;

    @PostMapping("/trip-info")
    public Map<String, Object> extractTripInfo(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        return tripInfoExtractionService.extract(text);
    }


}
