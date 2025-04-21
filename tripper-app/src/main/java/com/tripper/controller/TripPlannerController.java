package com.tripper.controller;

import com.tripper.model.TripRequest;
import com.tripper.model.TripResponse;
import com.tripper.service.TripPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling trip planning requests.
 * This class processes incoming trip planning requests and returns the response
 */
@RestController
@RequestMapping("/trip-planner")
@RequiredArgsConstructor
public class TripPlannerController {

    private final TripPlannerService tripPlannerService;

    @PostMapping
    public TripResponse handleTripPlanning(@RequestBody TripRequest request) {
        return tripPlannerService.processTripRequest(request);
    }
}
