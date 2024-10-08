package org.example.customer.Controller;

import org.example.library.dto.GenreDto;
import org.example.library.dto.TrackDto;
import org.example.library.model.Genre;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.TrackRepository;
import org.example.library.service.GenreService;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private GenreService genreService;

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

    @GetMapping("/{id}")
    public ResponseEntity<TrackDto> getTrackById(@PathVariable Long id) {
        TrackDto trackDto = trackService.getTrackById(id);
        return new ResponseEntity<>(trackDto, HttpStatus.OK);
    }




//    @PutMapping("/{id}")
//    public ResponseEntity<TrackDto> updateTrack(@PathVariable Long id,
//                                                @RequestParam("trackDto") TrackDto trackDto,
//                                                @RequestParam(value = "musicFile", required = false) MultipartFile musicFile) {
//        TrackDto updatedTrack = trackService.updateTrack(id, trackDto, musicFile);
//        return new ResponseEntity<>(updatedTrack, HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.deleteTrack(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TrackDto>> getTracksByUserId(@PathVariable Long userId) {
        List<TrackDto> tracks = trackService.getTracksByUserId(userId);
        if (tracks.isEmpty()) {
            System.out.println("No tracks found for userId: " + userId);
        } else {
            System.out.println("Found " + tracks.size() + " tracks for userId: " + userId);
        }

        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @GetMapping("/getAllGenre")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<GenreDto> genres = genreService.findAll();
        return ResponseEntity.ok(genres);
    }
}
