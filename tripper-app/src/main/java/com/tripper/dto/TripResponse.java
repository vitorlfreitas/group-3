package com.tripper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tripper.model.TripChecklistSection;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TripResponse(
        String userName,
        String recommendations,
        List<TripChecklistSection> sections,
        String pdfFileName
) {}
