package com.tripper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tripper.model.TripChecklistSection;
import java.util.List;

/**
 * TripResponse is a record that holds the response information for a trip.
 * It contains the user's name, recommendations, sections of the trip checklist,
 * and the name of the PDF file associated with the trip.
 *
 * @param userName The name of the user associated with the trip.
 * @param recommendations Recommendations for the trip.
 * @param sections A list of sections in the trip checklist.
 * @param pdfFileName The name of the PDF file associated with the trip.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TripResponse(
        String userName,
        String recommendations,
        List<TripChecklistSection> sections,
        String pdfFileName
) {}
