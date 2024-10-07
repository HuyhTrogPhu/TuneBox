package org.example.customer.controller;

import org.example.library.dto.TrackDto;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping
    public ResponseEntity<TrackDto> createTrack(@RequestBody TrackDto trackDto) {
        TrackDto createdTrack = trackService.createTrack(trackDto);
        return new ResponseEntity<>(createdTrack, HttpStatus.CREATED);
    }
}
