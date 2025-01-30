package com.example.controller;

import com.example.model.Location;
import com.example.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/save")
    public ResponseEntity<Location> save(@RequestBody Location location) {
        return new ResponseEntity<>(locationService.save(location), HttpStatus.CREATED);
    }

}
