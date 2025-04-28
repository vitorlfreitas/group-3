package com.tripper.service;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * SentenceDetectionService is a service class that detects sentences in a given paragraph.
 * It uses the OpenNLP library to perform sentence detection.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 * 
 * @see <a href="https://opennlp.apache.org/models.html">OpenNLP Models</a>
 */
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
