package org.example.customer.controller;

import org.example.library.dto.*;
import org.example.library.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/share")
public class ShareController {

    @Autowired
    private TrackService trackService;

    @Autowired
    private SendTrackService sendTrackService;

    @Autowired
    private AlbumsService albumsService;

    @Autowired
    private SendAlbumService sendAlbumService;

    @Autowired
    private SendPlaylistService sendPlaylistService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private SendInstrumentService sendInstrumentService;

    @Autowired
    private PostService postService;

    @Autowired
    private SendPostService sendPostService;

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);


    @GetMapping("/users/receivers")
    public ResponseEntity<List<ListUserForMessageDto>> getAllReceiversExcludingSender(@RequestParam Long userId) {
        try {
            List<ListUserForMessageDto> friends = messageService.findAllAcceptedFriends(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            logger.error("Error fetching friends for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // API tìm kiếm track
    @GetMapping("/track/{trackId}")
    public ResponseEntity<TrackDto> getTrackForSharing(@PathVariable Long trackId) {
        TrackDto track = trackService.getTrackById(trackId);
        return ResponseEntity.ok(track);
    }
    //API gửi track
    @PostMapping("/track")
    public ResponseEntity<Void> sendTrack(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam Long trackId) {
        sendTrackService.sendTrackMessage(senderId, receiverId, trackId);
        return ResponseEntity.ok().build();
    }

    // API tìm kiếm album
    @GetMapping("/album/{albumId}")
    public ResponseEntity<AlbumsDto> getAlbumForSharing(@PathVariable Long albumId) {
        AlbumsDto album = albumsService.getAlbumsById(albumId);
        return ResponseEntity.ok(album);
    }
    //API gửi album
    @PostMapping("/album")
    public ResponseEntity<Void> sendAlbum(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam Long albumId) {
        sendAlbumService.sendAlbumMessage(senderId, receiverId, albumId);
        return ResponseEntity.ok().build();
    }

    // API tìm kiếm playlist
    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<PlaylistDto> getPlaylistForSharing(@PathVariable Long playlistId) {
        PlaylistDto playlist = playlistService.getPlaylistById(playlistId);
        return ResponseEntity.ok(playlist);
    }

    //API gửi playlist
    @PostMapping("/playlist")
    public ResponseEntity<Void> sendPlaylist(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam Long playlistId) {
        sendPlaylistService.sendPlaylistMessage(senderId, receiverId, playlistId);
        return ResponseEntity.ok().build();
    }

    // API tìm kiếm instrument
    @GetMapping("/product/{productId}")
    public ResponseEntity<InstrumentDto> getProductForSharing(@PathVariable Long productId) {
        InstrumentDto instrument = instrumentService.getInstrumentById(productId);
        return ResponseEntity.ok(instrument);
    }

    //API gửi instrument
    @PostMapping("/product")
    public ResponseEntity<Void> sendProduct(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam Long productId) {
        sendInstrumentService.sendInstrumentMessage(senderId, receiverId, productId);
        return ResponseEntity.ok().build();
    }

    // API tìm kiếm Post
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDto> getPostForSharing(@PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    // API gửi Post
    @PostMapping("/post")
    public ResponseEntity<Void> sendPost(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam Long postId) {
        sendPostService.sendPostMessage(senderId, receiverId, postId);
        return ResponseEntity.ok().build();
    }
}