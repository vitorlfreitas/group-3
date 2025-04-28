package com.tripper.controller;

import com.tripper.model.TripRequest;
import com.tripper.dto.TripResponse;
import com.tripper.service.TripPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling trip planning requests.
 * It processes incoming trip requests and returns the corresponding trip response.
 * The controller is annotated with @RestController to indicate that it handles RESTful web service requests.
 * It uses @RequestMapping to define the base URL for the controller.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 */
@RestController
@RequestMapping("/trip-planner")
@RequiredArgsConstructor
public class TripPlannerController {

    private final TripPlannerService tripPlannerService;

    /**
     * Handles POST requests to the /trip-planner endpoint.
     * It processes the incoming TripRequest and returns a response.
     *
     * @param request The TripRequest object containing trip details.
     * @return ResponseEntity containing the TripResponse.
     */
    @PostMapping
    public ResponseEntity<TripResponse> handleTripPlanning(
            @RequestBody TripRequest request
    ) {
        TripResponse resp = tripPlannerService.processTripRequest(request);
        return ResponseEntity.ok(resp);
    }
}
