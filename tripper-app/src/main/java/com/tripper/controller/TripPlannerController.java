package com.tripper.controller;

import com.tripper.model.TripRequest;     // <-- back to model
import com.tripper.dto.TripResponse;
import com.tripper.service.TripPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip-planner")
@RequiredArgsConstructor
public class TripPlannerController {

    private final TripPlannerService tripPlannerService;

    @PostMapping
    public ResponseEntity<TripResponse> handleTripPlanning(
            @RequestBody TripRequest request
    ) {
        TripResponse resp = tripPlannerService.processTripRequest(request);
        return ResponseEntity.ok(resp);
    }
}
