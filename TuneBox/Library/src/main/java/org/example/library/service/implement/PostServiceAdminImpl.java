package org.example.library.service.implement;

import org.example.library.dto.*;
import org.example.library.model.Post;
import org.example.library.repository.PostAdminRepository;
import org.example.library.service.PostServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceAdminImpl implements PostServiceAdmin {

    @Autowired
    private PostAdminRepository postAdminRepository;

    private static final int TOP_POSTS_LIMIT = 5;


    private PostAdminDto convertToDto(Post post) {
        return new PostAdminDto(
                post.getId(),
                post.getContent(),
                post.getUser().getUserName(),
                post.getCreatedAt(),
                post.getLikes().size(),
                post.getComments().size(),
                post.getDescription(),
                post.isHidden()
        );
    }

    @Override
    public List<PostAdminDto> getTopPostsByInteractions() {
        // Lấy ngày bắt đầu của tháng hiện tại
        LocalDateTime startOfMonth = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();
        // Sử dụng Pageable để giới hạn kết quả trả về
        Pageable topFive = PageRequest.of(0, TOP_POSTS_LIMIT);

        // Lấy top 5 bài viết từ repository
        List<Post> posts = postAdminRepository.findTopPostsByInteractions(startOfMonth, topFive);

        // Chuyển đổi các bài viết thành DTO và trả về
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<PostAdminDto> findPostsWithImages() {
        List<Post> posts = postAdminRepository.findPostsWithImages();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findPostsWithoutImages() {
        List<Post> posts = postAdminRepository.findPostsWithoutImages();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findAllByOrderByCreatedAtDesc() {
        List<Post> posts = postAdminRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public PostStatisticsDto getPostStatistics() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);

        return PostStatisticsDto.builder()
                .totalPosts(postAdminRepository.count())
                .postsLastWeek(postAdminRepository.countPostsLastWeek(oneWeekAgo))
                .postsLastMonth(postAdminRepository.countPostsLastWeek(oneMonthAgo))
                .totalLikes(postAdminRepository.countTotalLikes())
                .totalComments(postAdminRepository.countTotalComments())
                .postsWithImages(postAdminRepository.countPostsWithImages())
                .postsWithoutImages(postAdminRepository.count() - postAdminRepository.countPostsWithImages())
                .averageLikesPerPost(postAdminRepository.calculateAverageLikesPerPost())
                .averageCommentsPerPost((double) postAdminRepository.countTotalComments() / postAdminRepository.count())
                .hiddenPosts(postAdminRepository.countHiddenPosts())
                .build();
    }

    @Override
    public List<PostEngagementDto> getPostEngagementStats() {
        List<Post> posts = postAdminRepository.findAll();
        return posts.stream()
                .map(post -> PostEngagementDto.builder()
                        .postId(post.getId())
                        .userName(post.getUser().getUserName())
                        .likeCount(post.getLikes().size())
                        .commentCount(post.getComments().size())
                        .shareCount(0) // Nếu không có tính năng share
                        .engagementRate(calculateEngagementRate(post))
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DailyPostStatsDto> getDailyPostStats() {
        LocalDateTime startDate = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        List<Object[]> results = postAdminRepository.findDailyPostStatsRaw(startDate);

        // Chuyển đổi Object[] thành DailyPostStatsDto
        return results.stream().map(row -> new DailyPostStatsDto(
                ((java.sql.Date) row[0]).toLocalDate(), // chuyển đổi java.sql.Date thành LocalDate
                ((Number) row[1]).intValue(), // postCount
                ((Number) row[2]).intValue(), // likeCount
                ((Number) row[3]).intValue(), // commentCount
                ((Number) row[4]).doubleValue() // interactionPercentage
        )).collect(Collectors.toList());
    }

    @Override
    public Long countPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return postAdminRepository.countPostsByDateRange(startDate, endDate);
    }

    @Override
    public List<PostEngagementDto> getEngagementStatsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return postAdminRepository.findEngagementStatsByDateRange(startDate, endDate);
    }


    public List<PostStatisticsResponseDto> getTopPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, String type) {
        List<Post> posts = postAdminRepository.findAll().stream()
                .filter(post -> post.getCreatedAt().isAfter(startDate) &&
                        post.getCreatedAt().isBefore(endDate))
                .collect(Collectors.toList());

        // Sắp xếp theo like hoặc comment
        if ("likes".equalsIgnoreCase(type)) {
            posts.sort((p1, p2) -> p2.getLikes().size() - p1.getLikes().size());
        } else if ("comments".equalsIgnoreCase(type)) {
            posts.sort((p1, p2) -> p2.getComments().size() - p1.getComments().size());
        } else {
            // Sắp xếp theo tổng tương tác (like + comment)
            posts.sort((p1, p2) -> {
                int p1Total = p1.getLikes().size() + p1.getComments().size();
                int p2Total = p2.getLikes().size() + p2.getComments().size();
                return p2Total - p1Total;
            });
        }

        return posts.stream()
                .limit(10)
                .map(this::convertToStatisticsDto)
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, Object> getStatsByCategory(String category) {
        return postAdminRepository.getStatsByCategory(category);
    }

    @Override
    public List<PostAdminDto> getTopPostsByCategory(String category) {
        return postAdminRepository.findAll().stream()
                .filter(post -> post.getCategory().equals(category))
                .sorted((p1, p2) -> Integer.compare(
                        p2.getLikes().size() + p2.getComments().size(),
                        p1.getLikes().size() + p1.getComments().size()))
                .limit(10)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInteractionDto> getMostActiveUsers() {
        return postAdminRepository.findMostActiveUsers();
    }

    @Override
    public List<PostAdminDto> getMostLikedPosts() {
        return postAdminRepository.findAll().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getLikes().size(), p1.getLikes().size()))
                .limit(10)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> getMostCommentedPosts() {
        return postAdminRepository.findAll().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getComments().size(), p1.getComments().size()))
                .limit(10)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getImagePostsStats() {
        // Sử dụng trực tiếp repository method đã có
        return postAdminRepository.getImagePostsStats();
    }

    public Map<String, Object> getImagePostStatsWithEngagementRate() {
        // Lấy thông tin từ phương thức getImagePostsStats()
        Map<String, Object> stats = postAdminRepository.getImagePostsStats();

        // Lấy tổng số bài viết
        Long totalPosts = postAdminRepository.countTotalPosts();

        // Tính engagementRate nếu tổng bài viết khác 0
        double engagementRate = totalPosts != 0 ?
                ((Double) stats.get("avgLikes") + (Double) stats.get("avgComments")) * 100.0 / totalPosts
                : 0.0;

        // Thêm engagementRate vào kết quả trả về
        stats.put("engagementRate", engagementRate);

        return stats;
    }

    @Override
    public Map<String, Object> getTextOnlyPostsStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Post> textOnlyPosts = postAdminRepository.findPostsWithoutImages();

        double totalPosts = textOnlyPosts.size();
        double totalLikes = textOnlyPosts.stream()
                .mapToInt(p -> p.getLikes().size())
                .sum();
        double totalComments = textOnlyPosts.stream()
                .mapToInt(p -> p.getComments().size())
                .sum();

        stats.put("count", totalPosts);
        stats.put("avgLikes", totalPosts > 0 ? totalLikes / totalPosts : 0);
        stats.put("avgComments", totalPosts > 0 ? totalComments / totalPosts : 0);
        stats.put("engagementRate", totalPosts > 0 ?
                (totalLikes + totalComments) * 100 / postAdminRepository.count() : 0);

        return stats;
    }

    @Override
    public Map<String, Object> getMixedContentPostsStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Post> allPosts = postAdminRepository.findAll();
        List<Post> mixedContentPosts = allPosts.stream()
                .filter(p -> !p.getImages().isEmpty() && !p.getContent().trim().isEmpty())
                .collect(Collectors.toList());

        double totalPosts = mixedContentPosts.size();
        double totalLikes = mixedContentPosts.stream()
                .mapToInt(p -> p.getLikes().size())
                .sum();
        double totalComments = mixedContentPosts.stream()
                .mapToInt(p -> p.getComments().size())
                .sum();

        stats.put("count", totalPosts);
        stats.put("avgLikes", totalPosts > 0 ? totalLikes / totalPosts : 0);
        stats.put("avgComments", totalPosts > 0 ? totalComments / totalPosts : 0);
        stats.put("engagementRate", totalPosts > 0 ?
                (totalLikes + totalComments) * 100 / allPosts.size() : 0);

        return stats;
    }

    @Override
    public Map<String, Long> getHourlyPostingPatterns() {
        List<Object[]> rawPatterns = postAdminRepository.findHourlyPostingPatternsRaw();
        Map<String, Long> patterns = new HashMap<>();
        for (Object[] row : rawPatterns) {
            patterns.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }
        return patterns;
    }

    @Override
    public Map<String, Long> getDailyPostingPatterns() {
        List<Object[]> rawPatterns = postAdminRepository.findDailyPostingPatternsRaw();
        Map<String, Long> patterns = new HashMap<>();
        for (Object[] row : rawPatterns) {
            patterns.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }
        return patterns;
    }

    @Override
    public Map<String, Long> getWeeklyPostingPatterns() {
        LocalDateTime startOfYear = LocalDateTime.now()
                .withMonth(1)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);
        List<Object[]> rawPatterns = postAdminRepository.findWeeklyPostingPatternsRaw(startOfYear);
        Map<String, Long> patterns = new HashMap<>();
        for (Object[] row : rawPatterns) {
            patterns.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }
        return patterns;
    }


    private double calculateEngagementRate(Post post) {
        int totalInteractions = post.getLikes().size() + post.getComments().size();
        return (double) totalInteractions / postAdminRepository.count() * 100;
    }


    // Thống kê theo ngày
    public List<PostStatisticsResponseDto> getTopPostsByDay(LocalDate date, String type) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return getTopPostsByDateRange(startOfDay, endOfDay, type);
    }

    // Thống kê theo tuần
    public List<PostStatisticsResponseDto> getTopPostsByWeek(LocalDate date, String type) {
        LocalDateTime startOfWeek = date.atStartOfDay().with(DayOfWeek.MONDAY);
        LocalDateTime endOfWeek = date.atTime(23, 59, 59).with(DayOfWeek.SUNDAY);
        return getTopPostsByDateRange(startOfWeek, endOfWeek, type);
    }

    // Thống kê theo tháng
    public List<PostStatisticsResponseDto> getTopPostsByMonth(YearMonth yearMonth, String type) {
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        return getTopPostsByDateRange(startOfMonth, endOfMonth, type);
    }

    // Thống kê theo năm
    public List<PostStatisticsResponseDto> getTopPostsByYear(int year, String type) {
        LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(year, 12, 31).atTime(23, 59, 59);
        return getTopPostsByDateRange(startOfYear, endOfYear, type);
    }

    // Thống kê giữa hai ngày
    public List<PostStatisticsResponseDto> getTopPostsBetweenDates(LocalDate startDate, LocalDate endDate, String type) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        return getTopPostsByDateRange(start, end, type);
    }

    // Lấy dữ liệu thống kê theo ngày để vẽ biểu đồ
    public Map<String, Object> getDailyStatisticsForChart(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> chartData = new ArrayList<>();

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            LocalDateTime dayStart = current.atStartOfDay();
            LocalDateTime dayEnd = current.atTime(23, 59, 59);

            List<Post> dayPosts = postAdminRepository.findAll().stream()
                    .filter(post -> post.getCreatedAt().isAfter(dayStart) &&
                            post.getCreatedAt().isBefore(dayEnd))
                    .collect(Collectors.toList());

            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", current.toString());
            dayData.put("totalPosts", dayPosts.size());
            dayData.put("totalLikes", dayPosts.stream().mapToInt(p -> p.getLikes().size()).sum());
            dayData.put("totalComments", dayPosts.stream().mapToInt(p -> p.getComments().size()).sum());
            dayData.put("avgLikes", dayPosts.isEmpty() ? 0 :
                    dayPosts.stream().mapToInt(p -> p.getLikes().size()).average().getAsDouble());
            dayData.put("avgComments", dayPosts.isEmpty() ? 0 :
                    dayPosts.stream().mapToInt(p -> p.getComments().size()).average().getAsDouble());

            chartData.add(dayData);
            current = current.plusDays(1);
        }

        result.put("chartData", chartData);
        result.put("summary", calculateSummaryStats(chartData));

        return result;
    }

    private Map<String, Object> calculateSummaryStats(List<Map<String, Object>> chartData) {
        Map<String, Object> summary = new HashMap<>();

        double totalPosts = chartData.stream()
                .mapToInt(data -> (Integer) data.get("totalPosts"))
                .sum();
        double totalLikes = chartData.stream()
                .mapToInt(data -> (Integer) data.get("totalLikes"))
                .sum();
        double totalComments = chartData.stream()
                .mapToInt(data -> (Integer) data.get("totalComments"))
                .sum();

        summary.put("totalPosts", totalPosts);
        summary.put("totalLikes", totalLikes);
        summary.put("totalComments", totalComments);
        summary.put("avgLikesPerDay", totalLikes / chartData.size());
        summary.put("avgCommentsPerDay", totalComments / chartData.size());
        summary.put("avgPostsPerDay", totalPosts / chartData.size());

        return summary;
    }


    private PostStatisticsResponseDto convertToStatisticsDto(Post post) {
        return PostStatisticsResponseDto.builder()
                .postId(post.getId())
                .content(post.getContent())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .totalInteractions(post.getLikes().size() + post.getComments().size())
                .build();
    }
}