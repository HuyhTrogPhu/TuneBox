package org.example.socialadmin.controller;

import org.example.library.dto.CommentDTO;
import org.example.library.dto.ReplyDto;
import org.example.library.model.Albums;
import org.example.library.model.Playlist;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.ReportRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
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

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ReportService reportService;

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
    @GetMapping("/getUserPlayList/{id}")
    public ResponseEntity<?> GetUserPlayList(@PathVariable("id") Long UserId){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", playlistService.getbyUserId(UserId));

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

// Thống kê User tạo theo tuần
    @GetMapping("/getUserBeetWeenWeek/{startDate}/{endDate}")
    public ResponseEntity<?> getUserBeetWeenWeek(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            int startYear = Integer.parseInt(startDateStr.substring(0, 4));
            int startWeek = Integer.parseInt(startDateStr.substring(6));
            int endYear = Integer.parseInt(endDateStr.substring(0, 4));
            int endWeek = Integer.parseInt(endDateStr.substring(6));

            LocalDate startDate = LocalDate.of(startYear, 1, 1).with(WeekFields.ISO.weekOfYear(), startWeek);
            LocalDate endDate = LocalDate.of(endYear, 1, 1).with(WeekFields.ISO.weekOfYear(), endWeek);

            Map<LocalDate, Long> userCountMap = userService.countUsersByWeekRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }


    // Thống kê User tạo theo tháng
    @GetMapping("/getUserBetweenMonth/{startDate}/{endDate}")
    public ResponseEntity<?> getUserBetweenMonth(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth startDate = YearMonth.parse(startDateStr, formatter);
            YearMonth endDate = YearMonth.parse(endDateStr, formatter);

            // Gọi hàm đếm user theo tháng trong userService
            Map<YearMonth, Long> userCountMap = userService.countUsersByMonthRange(startDate, endDate);

            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
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


    // Thống kê Track tạo theo tuần
    @GetMapping("/getTrackBeetWeenWeek/{startDate}/{endDate}")
    public ResponseEntity<?> getTrackBeetWeenWeek(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            int startYear = Integer.parseInt(startDateStr.substring(0, 4));
            int startWeek = Integer.parseInt(startDateStr.substring(6));
            int endYear = Integer.parseInt(endDateStr.substring(0, 4));
            int endWeek = Integer.parseInt(endDateStr.substring(6));

            LocalDate startDate = LocalDate.of(startYear, 1, 1).with(WeekFields.ISO.weekOfYear(), startWeek);
            LocalDate endDate = LocalDate.of(endYear, 1, 1).with(WeekFields.ISO.weekOfYear(), endWeek);

            Map<LocalDate, Long> userCountMap = trackService.countUsersByWeekRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }


    // Thống kê Track tạo theo tháng
    @GetMapping("/getTrackBetweenMonth/{startDate}/{endDate}")
    public ResponseEntity<?> getTrackBetweenMonth(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth startDate = YearMonth.parse(startDateStr, formatter);
            YearMonth endDate = YearMonth.parse(endDateStr, formatter);
            Map<YearMonth, Long> userCountMap = trackService.countUsersByMonthRange(startDate, endDate);

            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    // thong ke reports
    @GetMapping("/getTrackReport")
    public ResponseEntity<?> getTrackReport() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", reportRepository.findAllReportsWithTracks());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getAlbumReport")
    public ResponseEntity<?> getAlbumReport() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", reportRepository.findAllReportsWithAlbum());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getPostReport")
    public ResponseEntity<?> getPostReport() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", reportRepository.findAllReportsWithPost());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    //Album Static

    //thong ke Album tạo theo ngày
    @GetMapping("/getAlbumBeetWeen/{startDate}/{endDate}")
    public ResponseEntity<?> GetAlbumBeetWeen(@PathVariable("startDate") String startDateStr,
                                             @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            Map<LocalDate, Long> userCountMap = albumService.countUsersByDateRange(startDate, endDate);
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

    // Thống kê Album tạo theo tuần
    @GetMapping("/getAlbumBeetWeenWeek/{startDate}/{endDate}")
    public ResponseEntity<?> getAlbumBeetWeenWeek(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            int startYear = Integer.parseInt(startDateStr.substring(0, 4));
            int startWeek = Integer.parseInt(startDateStr.substring(6));
            int endYear = Integer.parseInt(endDateStr.substring(0, 4));
            int endWeek = Integer.parseInt(endDateStr.substring(6));

            LocalDate startDate = LocalDate.of(startYear, 1, 1).with(WeekFields.ISO.weekOfYear(), startWeek);
            LocalDate endDate = LocalDate.of(endYear, 1, 1).with(WeekFields.ISO.weekOfYear(), endWeek);

            Map<LocalDate, Long> userCountMap = albumService.countUsersByWeekRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }


    // Thống kê Album tạo theo tháng
    @GetMapping("/getAlbumBetweenMonth/{startDate}/{endDate}")
    public ResponseEntity<?> getAlbumBetweenMonth(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth startDate = YearMonth.parse(startDateStr, formatter);
            YearMonth endDate = YearMonth.parse(endDateStr, formatter);

            // Gọi hàm đếm user theo tháng trong userService
            Map<YearMonth, Long> userCountMap = albumService.countUsersByMonthRange(startDate, endDate);

            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    //PlayList static

    //thong ke PlayList tạo theo ngày
    @GetMapping("/getPlayListBeetWeen/{startDate}/{endDate}")
    public ResponseEntity<?> GetPlayListBeetWeen(@PathVariable("startDate") String startDateStr,
                                             @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            Map<LocalDate, Long> userCountMap = playlistService.countUsersByDateRange(startDate, endDate);
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

    // Thống kê PlayList tạo theo tuần
    @GetMapping("/getPlayListBeetWeenWeek/{startDate}/{endDate}")
    public ResponseEntity<?> getPlayListBeetWeenWeek(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            int startYear = Integer.parseInt(startDateStr.substring(0, 4));
            int startWeek = Integer.parseInt(startDateStr.substring(6));
            int endYear = Integer.parseInt(endDateStr.substring(0, 4));
            int endWeek = Integer.parseInt(endDateStr.substring(6));

            LocalDate startDate = LocalDate.of(startYear, 1, 1).with(WeekFields.ISO.weekOfYear(), startWeek);
            LocalDate endDate = LocalDate.of(endYear, 1, 1).with(WeekFields.ISO.weekOfYear(), endWeek);

            Map<LocalDate, Long> userCountMap = playlistService.countUsersByWeekRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }


    // Thống kê PlayList tạo theo tháng
    @GetMapping("/getPlayListBetweenMonth/{startDate}/{endDate}")
    public ResponseEntity<?> getPlayListBetweenMonth(@PathVariable("startDate") String startDateStr,
                                                 @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth startDate = YearMonth.parse(startDateStr, formatter);
            YearMonth endDate = YearMonth.parse(endDateStr, formatter);

            // Gọi hàm đếm user theo tháng trong userService
            Map<YearMonth, Long> userCountMap = playlistService.countUsersByMonthRange(startDate, endDate);

            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userCountMap);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    // Thống kê nguoi dung dang nhieu track nhat
    @GetMapping("/getMostTrackUploader/{startDate}/{endDate}")
    public ResponseEntity<?> getMostTrackUploader(  @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Successful");
            response.put("data", userService.getTop10UsersWithMostTracks(startDate, endDate));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    //detail rp for track
    @GetMapping("/getReport/{id}")
    public ResponseEntity<?> GetTrackReportById(@PathVariable("id") Long Id){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", reportRepository.findById(Id));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
    //ban theo rp ID
    @PutMapping("/ApproveRPTrack/{id}")
    public ResponseEntity<?> ApproveRPTrack(@PathVariable("id") Long Id){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",reportService.updateApprove(Id));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    //ban theo rp ID
    @PutMapping("/DeniedRPTrack/{id}")
    public ResponseEntity<?> DeniedRPTrack(@PathVariable("id") Long Id){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",reportService.updateDenied(Id));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    //followed nhiều nhất
    @GetMapping("/getMostFollowed")
    public ResponseEntity<?> GetMostFollowed(){
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data",userService.getTop10MostFollowedUsers());
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
// load for table
//user
@GetMapping("/getUserToTable/{startDate}/{endDate}")
public ResponseEntity<?> getUserToTable(@PathVariable("startDate") String startDateStr,
                                             @PathVariable("endDate") String endDateStr) {
    Map<String, Object> response = new HashMap<>();
    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        response.put("status", true);
        response.put("message", "Succesfull");
        response.put("data", userService.getUsersByDateRange(startDate, endDate));
    } catch (Exception ex) {
        response.put("status", false);
        response.put("message", ex);
        response.put("data", null);
    }
    return ResponseEntity.ok(response);
}
    //track
        @GetMapping("/getTrackToTable/{startDate}/{endDate}")
    public ResponseEntity<?> getTrackToTable(@PathVariable("startDate") String startDateStr,
                                            @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            List<Track> users =trackService.getTracksByDateRange(startDate, endDate);
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", users);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
    //Album
    @GetMapping("/getAlbumToTable/{startDate}/{endDate}")
    public ResponseEntity<?> getAlbumToTable(@PathVariable("startDate") String startDateStr,
                                            @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", albumService.getAlbumsByDateRange(startDate, endDate));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
    //PlayList
    @GetMapping("/getPlayListToTable/{startDate}/{endDate}")
    public ResponseEntity<?> getPlayListToTable(@PathVariable("startDate") String startDateStr,
                                            @PathVariable("endDate") String endDateStr) {
        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", playlistService.getPlaylistsByDateRange(startDate, endDate));
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
