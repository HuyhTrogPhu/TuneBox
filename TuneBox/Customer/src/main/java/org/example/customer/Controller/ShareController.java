    package org.example.customer.controller;

    import org.example.library.dto.AlbumsDto;
    import org.example.library.dto.PlaylistDto;
    import org.example.library.dto.TrackDto;
    //import org.example.library.dto.UserDTO;
    import org.example.library.dto.UserMessageDTO;
    import org.example.library.service.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @RequestMapping("/api/share")
    public class ShareController {

        @Autowired
        private UserService userService;

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

        @GetMapping("/users/receivers")
        public ResponseEntity<List<UserMessageDTO>> getAllReceiversExcludingSender(@RequestParam Long senderId) {
            List<UserMessageDTO> receivers = userService.findAllReceiversExcludingSender(senderId);
            return ResponseEntity.ok(receivers);
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

    }