package org.example.customer.Controller;

import org.example.library.dto.TrackDto;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @PostMapping
    public ResponseEntity<TrackDto> createTrack(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("musicFile") MultipartFile musicFile,
            @RequestParam("userId") Long userId) {

        // Tạo TrackDto
        TrackDto trackDto = new TrackDto();
        trackDto.setName(title);
        trackDto.setDescription(description);

        try {
            // Gọi service để lưu track
            TrackDto savedTrack = trackService.createTrack(trackDto, musicFile, userId);
            return new ResponseEntity<>(savedTrack, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDto> getTrackById(@PathVariable Long id) {
        TrackDto trackDto = trackService.getTrackById(id);
        return new ResponseEntity<>(trackDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TrackDto>> getAllTracks() {
        List<TrackDto> trackDtos = trackService.getAllTracks();
        return new ResponseEntity<>(trackDtos, HttpStatus.OK);
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
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }
}
