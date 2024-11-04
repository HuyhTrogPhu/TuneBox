package org.example.socialadmin.controller;

import org.example.library.dto.CommentDTO;
import org.example.library.dto.ReplyDto;
import org.example.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    CommentService commentService;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    ReplyService replyService;

    @Autowired
    UserInforService userInforService;


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
            response.put("data", userService.findAllUser());

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
            response.put("data",postService.findAllPosts());
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
            response.put("dataInformation", userService.findById(UserId));

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
    @GetMapping("/getAlbums/{id}")
    public ResponseEntity<?> GetAlbmumById(@PathVariable("id") Long Id){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("dataAlbums", albumService.findByAlbumsByID(Id));
            response.put("dataTracks",trackService.findByTracksByAlbumId(Id));

        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getAllAlbums")
    public ResponseEntity<?> GetAllAlbum(){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", albumService.getAll());

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

    @GetMapping("/getAllPlayList")
    public ResponseEntity<?> GetAllPlayList(){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", playlistService.findAll());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }
//get PLayList by ID
    @GetMapping("/getPLayList/{id}")
    public ResponseEntity<?> GetPLayListById(@PathVariable("id") Long Id){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",playlistService.findByPlaylistId(Id));
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
    //get user reported
    @GetMapping("/getUserReported")
    public ResponseEntity<?> GetUserReported(){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",userService.findByReportTrue());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    //get album reported
    @GetMapping("/getAlbumReported")
    public ResponseEntity<?> GetAlbumReported(){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",albumService.getAllReported());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
    //get Track reported
    @GetMapping("/getTrackReported")
    public ResponseEntity<?> GetTracksReported(){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",trackService.findReportedTrack());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
//thong ke user tạo theo ngày
@GetMapping("/getUserBeetWeen/{startDate}/{endDate}")
public ResponseEntity<?> GetUserBeetWeen(@PathVariable("startDate") String startDateStr,
                                         @PathVariable("endDate") String endDateStr) {
    Map<String, Object> response = new HashMap<>();
    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        Map<LocalDate, Long> userCountMap = userService.countUsersByDateRange(startDate, endDate);
        response.put("status", true);
        response.put("message", "Succesfull");
        response.put("data", userCountMap);
    } catch (Exception ex) {
        response.put("status", false);
        response.put("message", ex);
        response.put("data", null);
    }

    return ResponseEntity.ok(response);
}
// thong ke post tạo theo ngày
    @GetMapping("/getPostBeetWeen/{startDate}/{endDate}")
    public ResponseEntity<?> GetPostBeetWeen(@PathVariable("startDate") String startDateStr,
                                             @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Cập nhật định dạng nếu cần
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            Map<LocalDateTime, Long> userCountMap = postService.countPostByDateRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    // thong ke track tạo theo ngày
    @GetMapping("/getTrackBeetWeen/{startDate}/{endDate}")
    public ResponseEntity<?> GetTrackBeetWeen(@PathVariable("startDate") String startDateStr,
                                             @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            Map<LocalDate, Long> userCountMap = trackService.countTrackByDateRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
    // thong ke track tạo theo ngày/genre
    @GetMapping("/getTrackGenreBeetWeen/{startDate}/{endDate}")
    public ResponseEntity<?> GetTrackGenreBeetWeen(@PathVariable("startDate") String startDateStr,
                                              @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            Map<String, Long> userCountMap = trackService.getTrackCountsByGenreAndDateRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }



    //trộm của như
    @GetMapping("/getTrackCommentbyId/{trackId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTrack(@PathVariable Long trackId) {
        List<CommentDTO> comments = commentService.getCommentsByTrack(trackId);
        return ResponseEntity.ok(comments);
    }
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<ReplyDto>> getRepliesByComment(@PathVariable Long commentId) {
        List<ReplyDto> replies = replyService.getRepliesByComment(commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

}
