package com.tripper.controller;

import com.tripper.model.Message;
import com.tripper.model.TripChecklistSection;
import com.tripper.service.ConversationService;
import com.tripper.util.GPTResponseParser;
import com.tripper.util.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class NlpController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/generate-structured-pdf")
    public Map<String, String> createStructuredPdfFromChat(@RequestBody Map<String, String> request) {
        Long conversationId = Long.parseLong(request.get("conversationId"));
        String userName = request.getOrDefault("name", "Traveler");

        List<Message> messages = conversationService.getConversationMessages(conversationId);
        String fullChat = messages.stream()
                .map(m -> m.getSender() + ": " + m.getContent())
                .collect(Collectors.joining("\n"));

        List<TripChecklistSection> sections = GPTResponseParser.parseFromExampleResponse();

        String fileName = userName + "_StructuredTripChecklist.pdf";
        PDFGenerator.generateTripChecklistPdf(fileName, userName, sections);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Structured PDF generated from chat");
        response.put("file", fileName);
        return response;
    }
}
