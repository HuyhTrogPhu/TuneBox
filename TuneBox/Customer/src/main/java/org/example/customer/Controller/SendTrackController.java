    package org.example.customer.controller;

    import org.example.library.dto.TrackDto;
    //import org.example.library.dto.UserDTO;
    import org.example.library.dto.UserMessageDTO;
    import org.example.library.service.TrackService;
    import org.example.library.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @RequestMapping("/api/share")
    public class SendTrackController {

        @Autowired
        private UserService userService;

        @Autowired
        private TrackService trackService;

        @GetMapping("/users/receivers")
        public ResponseEntity<List<UserMessageDTO>> getAllReceiversExcludingSender(@RequestParam Long senderId) {
            List<UserMessageDTO> receivers = userService.findAllReceiversExcludingSender(senderId);
            return ResponseEntity.ok(receivers);
        }

        @GetMapping("/track/{trackId}")
        public ResponseEntity<TrackDto> getTrackForSharing(@PathVariable Long trackId) {
            TrackDto track = trackService.getTrackById(trackId);
            return ResponseEntity.ok(track);
        }
    }