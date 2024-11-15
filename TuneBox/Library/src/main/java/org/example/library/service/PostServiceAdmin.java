package org.example.library.service;

import org.example.library.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface PostServiceAdmin {
    List<PostAdminDto> getTopPostsByInteractions();

    List<PostAdminDto> findPostsWithImages();

    List<PostAdminDto> findPostsWithoutImages();

    List<PostAdminDto> findAllByOrderByCreatedAtDesc();

    PostStatisticsDto getPostStatistics();

    List<PostEngagementDto> getPostEngagementStats();

    List<DailyPostStatsDto> getDailyPostStats();

    // Thống kê theo khoảng thời gian
    Long countPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<PostEngagementDto> getEngagementStatsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<PostStatisticsResponseDto> getTopPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, String type);

    // Thống kê theo danh mục
    Map<String, Object> getStatsByCategory(String category);
    List<PostAdminDto> getTopPostsByCategory(String category);

    // Thống kê tương tác người dùng
    List<UserInteractionDto> getMostActiveUsers();
    List<PostAdminDto> getMostLikedPosts();
    List<PostAdminDto> getMostCommentedPosts();

    // Thống kê theo loại nội dung
    Map<String, Object> getImagePostsStats();
    Map<String, Object> getTextOnlyPostsStats();
    Map<String, Object> getMixedContentPostsStats();

    // Thống kê mẫu đăng bài
    Map<String, Long> getHourlyPostingPatterns();
    Map<String, Long> getDailyPostingPatterns();
    Map<String, Long> getWeeklyPostingPatterns();

    Map<String, Object> getImagePostStatsWithEngagementRate();

    List<PostStatisticsResponseDto> getTopPostsByDay(LocalDate date, String type);

    List<PostStatisticsResponseDto> getTopPostsByWeek(LocalDate date, String type);

    List<PostStatisticsResponseDto> getTopPostsByMonth(YearMonth yearMonth, String type);

    List<PostStatisticsResponseDto> getTopPostsByYear(int year, String type);

    List<PostStatisticsResponseDto> getTopPostsBetweenDates(LocalDate startDate, LocalDate endDate, String type);

    Map<String, Object> getDailyStatisticsForChart(LocalDate startDate, LocalDate endDate);
}
