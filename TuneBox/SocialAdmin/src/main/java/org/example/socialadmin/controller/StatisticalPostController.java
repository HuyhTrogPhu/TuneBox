package org.example.socialadmin.controller;

import org.example.library.dto.*;
import org.example.library.service.PostServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/social-statistical")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StatisticalPostController {

    @Autowired
    private PostServiceAdmin postServiceAdmin;

    // Get top trending posts
    @GetMapping("/trending-posts")
    public ResponseEntity<?> getTrendingPosts() {
        try {
            List<PostAdminDto> posts = postServiceAdmin.getTopPostsByInteractions();
            if (posts.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching trending posts: " + e.getMessage());
        }
    }

    // Get current statistics
    @GetMapping("/current-statistics")
    public ResponseEntity<?> getCurrentStatistics() {
        try {
            PostStatisticsDto statistics = postServiceAdmin.getPostStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }



    // Get post engagement statistics
    @GetMapping("/engagement-stats")
    public ResponseEntity<?> getEngagementStats() {
        try {
            List<PostEngagementDto> stats = postServiceAdmin.getPostEngagementStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Get posts by type (with/without images)
    @GetMapping("/posts-by-type/{type}")
    public ResponseEntity<?> getPostsByType(@PathVariable String type) {
        try {
            List<PostAdminDto> posts;
            if ("with-images".equals(type)) {
                posts = postServiceAdmin.findPostsWithImages();
            } else if ("without-images".equals(type)) {
                posts = postServiceAdmin.findPostsWithoutImages();
            } else {
                return ResponseEntity.badRequest().body("Invalid post type");
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Get daily post statistics
    @GetMapping("/daily-stats")
    public ResponseEntity<?> getDailyStats() {
        try {
            List<DailyPostStatsDto> stats = postServiceAdmin.getDailyPostStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats-by-date-range/{startDate}/{endDate}")
    public ResponseEntity<?> getStatsByDateRange(
            @PathVariable String startDate,
            @PathVariable String endDate) {
        try {
            // Parse các tham số đầu vào thành LocalDate
            LocalDate parsedStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate parsedEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Chuyển LocalDate thành LocalDateTime
            LocalDateTime startDateTime = parsedStartDate.atStartOfDay();  // Đặt thời gian là 00:00:00
            LocalDateTime endDateTime = parsedEndDate.atTime(23, 59, 59);  // Đặt thời gian là 23:59:59

            // Tạo response map để chứa các thống kê
            Map<String, Object> response = new HashMap<>();

            // Thêm các thống kê theo khoảng thời gian
            response.put("postCount", postServiceAdmin.countPostsByDateRange(startDateTime, endDateTime));
            response.put("engagementStats", postServiceAdmin.getEngagementStatsByDateRange(startDateTime, endDateTime));
            response.put("topPosts", postServiceAdmin.getTopPostsByDateRange(startDateTime, endDateTime, "all"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Get statistics by category
    @GetMapping("/stats-by-category/{category}")
    public ResponseEntity<?> getStatsByCategory(@PathVariable String category) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("categoryStats", postServiceAdmin.getStatsByCategory(category));
            response.put("topPostsInCategory", postServiceAdmin.getTopPostsByCategory(category));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Get user interaction statistics
    @GetMapping("/user-interaction-stats")
    public ResponseEntity<?> getUserInteractionStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("mostActiveUsers", postServiceAdmin.getMostActiveUsers());
            stats.put("mostLikedPosts", postServiceAdmin.getMostLikedPosts());
            stats.put("mostCommentedPosts", postServiceAdmin.getMostCommentedPosts());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Get content type statistics
    @GetMapping("/content-type-stats")
    public ResponseEntity<?> getContentTypeStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("imagePostsStats", postServiceAdmin.getImagePostsStats());
            stats.put("textOnlyPostsStats", postServiceAdmin.getTextOnlyPostsStats());
            stats.put("mixedContentStats", postServiceAdmin.getMixedContentPostsStats());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Get time-based posting patterns
    @GetMapping("/posting-patterns")
    public ResponseEntity<?> getPostingPatterns() {
        try {
            Map<String, Object> patterns = new HashMap<>();
            patterns.put("hourlyPatterns", postServiceAdmin.getHourlyPostingPatterns());
            patterns.put("dailyPatterns", postServiceAdmin.getDailyPostingPatterns());
            patterns.put("weeklyPatterns", postServiceAdmin.getWeeklyPostingPatterns());
            return ResponseEntity.ok(patterns);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/post-stats")
    public Map<String, Object> getPostStats() {
        return postServiceAdmin.getImagePostStatsWithEngagementRate();
    }

    @GetMapping("/daily/{date}")
    public ResponseEntity<List<PostStatisticsResponseDto>> getDailyTopPosts(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(defaultValue = "total") String type) {
        return ResponseEntity.ok(postServiceAdmin.getTopPostsByDay(date, type));
    }

    // Thống kê theo tuần
    @GetMapping("/weekly/{date}")
    public ResponseEntity<List<PostStatisticsResponseDto>> getWeeklyTopPosts(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(defaultValue = "total") String type) {
        return ResponseEntity.ok(postServiceAdmin.getTopPostsByWeek(date, type));
    }

    // Thống kê theo tháng
    @GetMapping("/monthly/{yearMonth}")
    public ResponseEntity<List<PostStatisticsResponseDto>> getMonthlyTopPosts(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
            @RequestParam(defaultValue = "total") String type) {
        return ResponseEntity.ok(postServiceAdmin.getTopPostsByMonth(yearMonth, type));
    }

    // Thống kê theo năm
    @GetMapping("/yearly/{year}")
    public ResponseEntity<List<PostStatisticsResponseDto>> getYearlyTopPosts(
            @PathVariable int year,
            @RequestParam(defaultValue = "total") String type) {
        return ResponseEntity.ok(postServiceAdmin.getTopPostsByYear(year, type));
    }

    // Thống kê giữa hai ngày
    @GetMapping("/range")
    public ResponseEntity<List<PostStatisticsResponseDto>> getTopPostsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "total") String type) {
        return ResponseEntity.ok(postServiceAdmin.getTopPostsBetweenDates(startDate, endDate, type));
    }

    // API để lấy dữ liệu cho biểu đồ
    @GetMapping("/chart-data")
    public ResponseEntity<Map<String, Object>> getChartData(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.ok(postServiceAdmin.getDailyStatisticsForChart(startDate, endDate));
    }
}