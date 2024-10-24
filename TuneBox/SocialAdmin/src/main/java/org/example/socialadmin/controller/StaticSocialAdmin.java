package org.example.socialadmin.controller;

import org.example.library.service.AlbumService;
import org.example.library.service.PostService;
import org.example.library.service.TrackService;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/SocialAdmin/static")
public class StaticSocialAdmin {
    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    AlbumService albumService;

    @Autowired
    TrackService trackService;


    @GetMapping("/countUser")
    public ResponseEntity<?> getCountUser() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "tong so user");
            response.put("data", userService.countUser());
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "UserData");
            response.put("data", userService.GetAll());
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllpost")
    public ResponseEntity<?> UserOderByPost() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "PostData");
            response.put("data",postService.getAllPosts());
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getNewPost")
    public ResponseEntity<?> Get5Post() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "PostData");
            response.put("data",postService.get5Posts());
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getPost/{id}")
    public ResponseEntity<?> GetPostById(@PathVariable("id") Long PostId) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "PostData");
            response.put("data",postService.getPostById(PostId));
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getUs/{id}")
    public ResponseEntity<?> GetUser(@PathVariable("id") Long UserId){
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", userService.findById(UserId));

        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Fail");
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUserAlbums/{id}")
    public ResponseEntity<?> GetUserAlbum(@PathVariable("id") Long UserId){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", albumService.getbyUserId(UserId));

        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getDetailTrack/{id}")
    public ResponseEntity<?> GetDetailTrack(@PathVariable("id") Long Id){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", trackService.getTrackCountCommentandLike(Id));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getAllTrack")
    public ResponseEntity<?> GetTrack(){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", trackService.getAll());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getTrack/{id}")
    public ResponseEntity<?> GetTrackById(@PathVariable("id") Long Id){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", trackService.getTrackById(Id));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }

}
