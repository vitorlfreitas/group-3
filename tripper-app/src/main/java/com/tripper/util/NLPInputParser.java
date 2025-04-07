package com.tripper.util;

import com.tripper.model.TripDetails;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NLPInputParser {

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME posTagger;
    private DictionaryLemmatizer lemmatizer;

    public NLPInputParser() {
        try {
            // Load sentence model
            InputStream sentenceModelIn = getClass().getClassLoader().getResourceAsStream("models/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin");
            SentenceModel sentModel = new SentenceModel(sentenceModelIn);
            sentenceDetector = new SentenceDetectorME(sentModel);
            sentenceModelIn.close();

            // Load tokenizer model
            InputStream tokenModelIn = getClass().getClassLoader().getResourceAsStream("models/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin");
            TokenizerModel tokenizerModel = new TokenizerModel(tokenModelIn);
            tokenizer = new TokenizerME(tokenizerModel);
            tokenModelIn.close();

            // Load POS tagger model
            InputStream posModelIn = getClass().getClassLoader().getResourceAsStream("models/opennlp-en-ud-ewt-pos-1.2-2.5.0.bin");
            POSModel posModel = new POSModel(posModelIn);
            posTagger = new POSTaggerME(posModel);
            posModelIn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TripDetails parseTripDetails(String input) {
        TripDetails details = new TripDetails();
        List<String> locations = new ArrayList<>();
        List<String> dateTokens = new ArrayList<>();

        String[] sentences = sentenceDetector.sentDetect(input);
        for (String sentence : sentences) {
            String[] tokens = tokenizer.tokenize(sentence);
            String[] tags = posTagger.tag(tokens);

            if (lemmatizer != null) {
                lemmatizer.lemmatize(tokens, tags); // just for demonstration
            }

            StringBuilder currentEntity = new StringBuilder();
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                String tag = tags[i];

                if (tag.equals("NNP") || tag.equals("NNPS")) {
                    if (currentEntity.length() > 0) currentEntity.append(" ");
                    currentEntity.append(token);
                } else {
                    if (currentEntity.length() > 0) {
                        locations.add(currentEntity.toString());
                        currentEntity.setLength(0);
                    }
                }

                if (token.matches("\\d{1,2}/\\d{1,2}/\\d{2,4}") ||
                        token.matches("(?i)(january|february|march|april|may|june|july|august|september|october|november|december).*")) {
                    dateTokens.add(token);
                }
            }

            if (currentEntity.length() > 0) {
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
