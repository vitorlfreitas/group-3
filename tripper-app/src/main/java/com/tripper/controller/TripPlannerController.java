package com.tripper.controller;

import com.tripper.model.TripRequest;
import com.tripper.model.TripResponse;
import com.tripper.service.TripPlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip")
public class TripPlannerController {

    @Autowired
    private TripPlannerService tripPlannerService;

    @PostMapping("/plan")
    public TripResponse planTrip(@RequestBody TripRequest request) {
        return tripPlannerService.processTripRequest(request);
    }
}
