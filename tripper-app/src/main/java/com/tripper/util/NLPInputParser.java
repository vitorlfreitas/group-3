package com.tripper.util;

import com.tripper.model.TripDetails;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NLPInputParser {

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME posTagger;
    private DictionaryLemmatizer lemmatizer; // optional

    public NLPInputParser() {
        try {
            // 1) Load sentence detection model
            InputStream sentenceModelIn = new FileInputStream("models/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin");
            SentenceModel sentModel = new SentenceModel(sentenceModelIn);
            sentenceDetector = new SentenceDetectorME(sentModel);
            sentenceModelIn.close();

            // 2) Load tokenization model
            InputStream tokenModelIn = new FileInputStream("models/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin");
            TokenizerModel tokenizerModel = new TokenizerModel(tokenModelIn);
            tokenizer = new TokenizerME(tokenizerModel);
            tokenModelIn.close();

            // 3) Load POS tagging model
            InputStream posModelIn = new FileInputStream("models/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin");
            POSModel posModel = new POSModel(posModelIn);
            posTagger = new POSTaggerME(posModel);
            posModelIn.close();

            // 4) (Optional) Load lemmatizer dictionary model
            // If you want to use lemmas, otherwise you can skip this
            try (InputStream lemmaModelIn = new FileInputStream("models/opennlp-en-ud-ewt-lemmas-1.2-2.5.0.bin")) {
                lemmatizer = new DictionaryLemmatizer(lemmaModelIn);
            } catch (Exception e) {
                // If file not found or not used, ignore or log
                System.out.println("Lemmatizer model not found, skipping lemmatization...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the input text to detect sentences, tokenize, and POS tag.
     * We can guess location names by looking for NNP/NNPS tags.
     */
    public TripDetails parseTripDetails(String input) {
        TripDetails details = new TripDetails();

        // 1) Sentence Detection
        String[] sentences = sentenceDetector.sentDetect(input);

        // We'll store all candidate "locations" in a list
        List<String> locations = new ArrayList<>();

        // We'll store any date-like tokens in a simple approach
        // (still can use a regex for date patterns if you want)
        List<String> dateTokens = new ArrayList<>();

        for (String sentence : sentences) {
            // 2) Tokenize the sentence
            String[] tokens = tokenizer.tokenize(sentence);

            // 3) POS Tag the tokens
            String[] tags = posTagger.tag(tokens);

            // (Optional) Lemmatize tokens
            // If the lemmatizer is loaded, we can get lemmas
            if (lemmatizer != null) {
                String[] lemmas = lemmatizer.lemmatize(tokens, tags);
                // For demonstration, not used further
            }

            // 4) Identify NNP/NNPS as potential location names
            StringBuilder currentEntity = new StringBuilder();
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                String tag = tags[i];

                if (tag.equals("NNP") || tag.equals("NNPS")) {
                    if (currentEntity.length() > 0) {
                        currentEntity.append(" ");
                    }
                    currentEntity.append(token);
                } else {
                    if (currentEntity.length() > 0) {
                        locations.add(currentEntity.toString());
                        currentEntity.setLength(0);
                    }
                }

                // Simple approach: check if token looks like a date
                if (token.matches("\\d{1,2}/\\d{1,2}/\\d{2,4}") || token.matches("(?i)(january|february|march|april|may|june|july|august|september|october|november|december).*")) {
                    dateTokens.add(token);
                }
            }
            // End of sentence: flush any trailing NNP entity
            if (currentEntity.length() > 0) {
                locations.add(currentEntity.toString());
            }
        }

        details.setLocations(locations);

        // Combine date tokens as a simple string
        if (!dateTokens.isEmpty()) {
            StringBuilder dateInfo = new StringBuilder();
            for (String dt : dateTokens) {
                dateInfo.append(dt).append(" ");
            }
            details.setTravelMonth(dateInfo.toString().trim());
        }

        return details;
    }
}
