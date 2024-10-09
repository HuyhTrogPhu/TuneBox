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


    //creat Track
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

    // update TRack
    @PutMapping("/{trackId}")
    public ResponseEntity<TrackDto> updateTrack(@PathVariable Long trackId,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "trackImage", required = false) MultipartFile trackImage,
                                                @RequestParam(value = "trackFile", required = false) MultipartFile trackFile,
                                                @RequestParam(value = "description", required = false) String description,
                                                @RequestParam(value = "status", required = false) Boolean status,
                                                @RequestParam(value = "report", required = false) Boolean report,
                                                @RequestParam(value = "genreId", required = false) Long genreId,
                                                @RequestParam(value = "userId", required = false) Long userId)  {

        try {
            TrackDto trackDto = new TrackDto();
            trackDto.setName(name);
            trackDto.setDescription(description);
            trackDto.setStatus(status);
            trackDto.setReport(report);
//          trackDto.setCreateDate(LocalDate.now());
//          trackDto.setReportDate(null);

            // Cập nhật track
            TrackDto updatedTrack = trackService.updateTrack(trackId, trackDto, trackImage, trackFile, userId, genreId);
            if (updatedTrack == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedTrack, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        trackService.deleteTrack(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // get Track theo id ngdung
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

    //get tat ca cac theloai
    @GetMapping("/getAllGenre")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<GenreDto> genres = genreService.findAll();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDto> getTrackById(@PathVariable Long id) {
        TrackDto trackDto = trackService.getTrackById(id);
        return new ResponseEntity<>(trackDto, HttpStatus.OK);
    }

}
