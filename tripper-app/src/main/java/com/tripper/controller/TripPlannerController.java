package com.tripper.controller;

import com.tripper.model.TripRequest;
import com.tripper.model.TripResponse;
import com.tripper.service.TripPlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/trip-planner")
public class TripPlannerController {

    @Autowired
    private TripPlannerService tripPlannerService;

    @PostMapping
    public TripResponse handleTripPlanning(@RequestBody TripRequest request) {
        return tripPlannerService.processTripRequest(request);
    }
}
