package org.example.customer.controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.dto.CommentDto;
import org.example.library.dto.TrackDto;
import org.example.library.model.Track;
import org.example.library.service.implement.TrackServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/track")
public class trackController {

    @Autowired
    private TrackServiceImpl trackService;

    // get all track
    @GetMapping("/getAllTrack")
    public ResponseEntity<List<TrackDto>> getAllTrack() {
        List<TrackDto> trackDto = trackService.getAllTrack();
        return ResponseEntity.ok(trackDto);
    }

    // get track by id
    @GetMapping("{trackId}")
    public ResponseEntity<TrackDto> getTrack(@PathVariable("trackId") Long trackId) {
        TrackDto trackDto = trackService.getTrackByID(trackId);
        return ResponseEntity.ok(trackDto);
    }

    // add new Track
    @PostMapping
    public ResponseEntity<TrackDto> createTrack(@RequestBody TrackDto trackDto,
                                                @RequestParam("imageTrack")MultipartFile image) {
        TrackDto saveTrack = trackService.creatTrack(trackDto, image);
        return new ResponseEntity<>(saveTrack, HttpStatus.CREATED);
    }

     // update track
    @PutMapping("{trackId}")
    public ResponseEntity<TrackDto> updateTrack(@RequestBody TrackDto trackDto,
                                                @PathVariable("trackId") Long id,
                                                @RequestParam("imageTrack") MultipartFile image){
        TrackDto saveTrack = trackService.updateTrack(id, trackDto, image);
        return ResponseEntity.ok(saveTrack);
    }

    // Delete Track
    @DeleteMapping("{trackId}")
    public ResponseEntity<String> deleteTrack(@PathVariable("trackId") Long trackId) {
        trackService.deleteTrack(trackId);
        return ResponseEntity.ok("Track deleted successfully");
    }

    // like Track
    @PostMapping("{trackId}/like")
    public ResponseEntity<String> likeTrack(@PathVariable("trackId") Long trackId,
                                            @RequestParam("userId") Long userId) {
        trackService.likeTrack(trackId, userId);
        return ResponseEntity.ok("Like successfully");
    }

    // comment Track
    @PostMapping("{trackId}/comment")
    public ResponseEntity<CommentDto> commentOnTrack(@PathVariable("trackId") Long trackId,
                                                     @RequestParam("userId") Long userId,
                                                     @RequestBody String content) {
        CommentDto newComment = trackService.commentOnTrack(trackId, userId, content);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
}
