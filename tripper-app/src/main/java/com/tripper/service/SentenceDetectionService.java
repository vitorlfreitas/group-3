package com.tripper.service;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class SentenceDetectionService {

    private SentenceDetectorME sentenceDetector;

    public SentenceDetectionService() {
        try {
            InputStream modelIn = new ClassPathResource("models/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin").getInputStream();
            SentenceModel model = new SentenceModel(modelIn);
            sentenceDetector = new SentenceDetectorME(model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String[] detectSentences(String paragraph) {
        return sentenceDetector.sentDetect(paragraph);
    }
}
