package com.myway.myway.web.rest;

import com.myway.myway.service.LocationSearchService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationSearchController {

    private final LocationSearchService locationSearchService;

    @Autowired
    public LocationSearchController(LocationSearchService locationSearchService) {
        this.locationSearchService = locationSearchService;
    }

    @GetMapping("/search")
    public String searchLocation(@RequestParam String query) throws IOException {
        return locationSearchService.searchLocation(query);
    }
}
