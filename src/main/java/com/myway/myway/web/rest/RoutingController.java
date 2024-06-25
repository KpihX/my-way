package com.myway.myway.web.rest;

import com.myway.myway.service.RoutingService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routing")
public class RoutingController {

    private final RoutingService routingService;

    @Autowired
    public RoutingController(RoutingService routingService) {
        this.routingService = routingService;
    }

    @PostMapping("/calculate")
    public String calculateRoute(@RequestParam String travelMode, @RequestBody List<double[]> selectedPlaces) throws IOException {
        //"driving" ou "walking", "cycling"
        return routingService.calculateRoute(travelMode, selectedPlaces);
    }
}
