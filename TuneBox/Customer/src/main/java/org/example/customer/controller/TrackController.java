package org.example.customer.controller;


import lombok.AllArgsConstructor;
import org.example.library.dto.TrackDto;
import org.example.library.model.Genre;
import org.example.library.model.User;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/customer/track")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @PostMapping
    public ResponseEntity<TrackDto> createTrack(@RequestParam("name") String name, @RequestParam("trackImage") MultipartFile trackImage,
                                                @RequestParam("trackFile") MultipartFile trackFile, @RequestParam("description") String description,
                                                @RequestParam("status") boolean status,
                                                @RequestParam("report") boolean report,
                                                @RequestParam("genre") Genre genre, @RequestParam("user") User user) {

        try {
            TrackDto trackDto = new TrackDto();
            trackDto.setName(name);
            trackDto.setDescription(description);
            trackDto.setStatus(status);
            trackDto.setReport(report);
            trackDto.setCreateDate(LocalDate.now());
            trackDto.setReportDate(null); // Assume report date is null for now

            TrackDto createdTrack = trackService.createTrack(trackDto, trackImage, trackFile, user.getId(), genre.getId());
            return new  ResponseEntity<>(createdTrack, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
