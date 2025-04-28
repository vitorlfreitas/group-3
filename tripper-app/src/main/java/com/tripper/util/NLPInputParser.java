package com.tripper.util;

import com.tripper.model.TripDetails;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * NLPInputParser is a utility class that uses OpenNLP to parse input text
 * and extract trip details such as locations and travel month.
 * It initializes the necessary models for sentence detection, tokenization,
 * and part-of-speech tagging.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 * 
 * @see <a href="https://opennlp.apache.org/models.html">OpenNLP Models</a>
 */
@Component
public class NLPInputParser {

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME posTagger;

    /**
     * Constructor that initializes the NLP models for sentence detection,
     * tokenization, and part-of-speech tagging.
     */
    public NLPInputParser() {

        try {
            // Sentence model
            try (InputStream sentenceModelIn = getClass().getClassLoader().getResourceAsStream("models/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin")) {
                if (sentenceModelIn == null) {
                    throw new IllegalStateException("Sentence model not found");
                }
                SentenceModel sentModel = new SentenceModel(sentenceModelIn);
                sentenceDetector = new SentenceDetectorME(sentModel);
            }

            // Tokenizer model
            try (InputStream tokenModelIn = getClass().getClassLoader().getResourceAsStream("models/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin")) {
                if (tokenModelIn == null) {
                    throw new IllegalStateException("Tokenizer model not found");
                }
                TokenizerModel tokenizerModel = new TokenizerModel(tokenModelIn);
                tokenizer = new TokenizerME(tokenizerModel);
            }

            // POS tagger model
            try (InputStream posModelIn = getClass().getClassLoader().getResourceAsStream("models/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin")) {
                if (posModelIn == null) {
                    throw new IllegalStateException("POS tagger model not found");
                }
                POSModel posModel = new POSModel(posModelIn);
                posTagger = new POSTaggerME(posModel);
            }


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Parses the input text to extract trip details such as locations and travel month.
     *
     * @param input the input text containing trip details
     * @return a TripDetails object containing the extracted locations and travel month
     */
    public TripDetails parseTripDetails(String input) {
        TripDetails details = new TripDetails();
        List<String> locations = new ArrayList<>();
        List<String> dateTokens = new ArrayList<>();

        String[] sentences = sentenceDetector.sentDetect(input);
        for (String sentence : sentences) {
            String[] tokens = tokenizer.tokenize(sentence);
            String[] tags = posTagger.tag(tokens);

            StringBuilder currentEntity = new StringBuilder();
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                String tag = tags[i];

                if (tag.equals("NNP") || tag.equals("NNPS")) {
                    if (!currentEntity.isEmpty()) currentEntity.append(" ");
                    currentEntity.append(token);
                } else {
                    if (!currentEntity.isEmpty()) {
                        locations.add(currentEntity.toString());
                        currentEntity.setLength(0);
                    }
                }

                if (token.matches("\\d{1,2}/\\d{1,2}/\\d{2,4}") ||
                        token.matches("(?i)(january|february|march|april|may|june|july|august|september|october|november|december).*")) {
                    dateTokens.add(token);
                }
            }

            if (!currentEntity.isEmpty()) {
                locations.add(currentEntity.toString());
            }
        }

        details.setLocations(locations);
        if (!dateTokens.isEmpty()) {
            details.setTravelMonth(String.join(" ", dateTokens));
        }

        return details;
    }
}
