package com.tripper.controller;

import com.tripper.model.TripChecklistSection;
import com.tripper.service.SentenceDetectionService;
import com.tripper.service.TripChatService;
import com.tripper.service.TripInfoExtractionService;
import com.tripper.util.GPTResponseParser;
import com.tripper.util.PDFGenerator;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private TripChatService tripChatService;

    @PostMapping("/trip-recommendations")
    public Map<String, Object> getTripAdvice(@RequestBody Map<String, Object> request) {
        String name = (String) request.getOrDefault("name", "Traveler");
        List<String> locations = (List<String>) request.get("locations");
        List<String> dates = (List<String>) request.get("dates");

        String gptResponse = tripChatService.generateTripAdvice(name, locations, dates);

        Map<String, Object> response = new HashMap<>();
        response.put("recommendations", gptResponse);
        return response;
    }

    @PostMapping("/generate-structured-pdf")
    public Map<String, String> createStructuredPdf(@RequestBody Map<String, String> request) {
        String name = request.getOrDefault("name", "Traveler");
        String fileName = name + "_StructuredTripChecklist.pdf";

        List<TripChecklistSection> sections = GPTResponseParser.parseFromExampleResponse();
        PDFGenerator.generateTripChecklistPdf(fileName, name, sections);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Structured PDF generated");
        response.put("file", fileName);
        return response;
    }



}
