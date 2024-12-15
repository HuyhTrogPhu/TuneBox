package org.example.library.repository;

import org.example.library.dto.PostEngagementDto;
import org.example.library.dto.UserInteractionDto;
import org.example.library.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface PostAdminRepository extends JpaRepository<Post, String> {

    @Query("SELECT p FROM Post p " +
            "WHERE p.createdAt >= :startDate " +
            "AND p.hidden = false " +
            "ORDER BY (SIZE(p.likes) + SIZE(p.comments)) DESC")
    List<Post> findTopPostsByInteractions(@Param("startDate") LocalDateTime startDate, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE SIZE(p.images) > 0")
    List<Post> findPostsWithImages();

    @Query("SELECT p FROM Post p WHERE SIZE(p.images) = 0")
    List<Post> findPostsWithoutImages();

    List<Post> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.hidden = true")
    Long countHiddenPosts();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :oneWeekAgo")
    Long countPostsLastWeek(@Param("oneWeekAgo") LocalDateTime oneWeekAgo);

    @Query("SELECT COUNT(l) FROM Post p JOIN p.likes l")
    Long countTotalLikes();

    @Query("SELECT COUNT(c) FROM Post p JOIN p.comments c")
    Long countTotalComments();

    @Query("SELECT COUNT(p) FROM Post p WHERE SIZE(p.images) > 0")
    Long countPostsWithImages();

    @Query("SELECT AVG(SIZE(p.likes)) FROM Post p")
    Double calculateAverageLikesPerPost();

    @Query(nativeQuery = true, value =
            "SELECT DATE(p.created_at) as date, " +
                    "COUNT(DISTINCT p.id) as post_count, " +
                    "COUNT(DISTINCT l.id) as like_count, " +
                    "COUNT(DISTINCT c.id) as comment_count, " +
                    "CAST((COUNT(DISTINCT l.id) + COUNT(DISTINCT c.id)) * 100.0 / " +
                    "NULLIF(COUNT(DISTINCT p.id), 0) AS DECIMAL(10,2)) as interaction_percentage " +
                    "FROM posts p " +
                    "LEFT JOIN likes l ON l.post_id = p.id " +
                    "LEFT JOIN comments c ON c.post_id = p.id " +
                    "WHERE p.created_at >= :startDate " +
                    "GROUP BY DATE(p.created_at) " +
                    "ORDER BY DATE(p.created_at)")
    List<Object[]> findDailyPostStatsRaw(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Long countPostsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT new org.example.library.dto.PostEngagementDto(p.id, p.user.userName, COUNT(l), COUNT(c), 0, p.createdAt) " +
            "FROM Post p LEFT JOIN p.likes l LEFT JOIN p.comments c " +
            "WHERE p.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id, p.user.userName, p.createdAt")
    List<PostEngagementDto> findEngagementStatsByDateRange(LocalDateTime startDate, LocalDateTime endDate);



    @Query("SELECT COUNT(p) as totalPosts, " +
            "SUM(SIZE(p.likes)) as totalLikes, " +
            "SUM(SIZE(p.comments)) as totalComments, " +
            "AVG(SIZE(p.likes) + SIZE(p.comments)) as averageEngagement " +
            "FROM Post p WHERE p.category = :category")
    Map<String, Object> getStatsByCategory(@Param("category") String category);

    @Query("SELECT p.user.id as userId, p.user.userName as userName, " +
            "COUNT(p) as postCount, " +
            "SUM(SIZE(p.likes)) as totalLikes, " +
            "SUM(SIZE(p.comments)) as totalComments " +
            "FROM Post p GROUP BY p.user.id, p.user.userName " +
            "ORDER BY COUNT(p) DESC")
    List<UserInteractionDto> findMostActiveUsers();

    @Query("SELECT COUNT(p) as count, " +
            "AVG(SIZE(p.likes)) as avgLikes, " +
            "AVG(SIZE(p.comments)) as avgComments " +
            "FROM Post p WHERE SIZE(p.images) > 0")
    Map<String, Object> getImagePostsStats();


    @Query("SELECT COUNT(p) FROM Post p")
    Long countTotalPosts();



    @Query(nativeQuery = true, value =
            "SELECT EXTRACT(HOUR FROM created_at) as hour, COUNT(*) as count " +
                    "FROM post " +
                    "GROUP BY EXTRACT(HOUR FROM created_at) " +
                    "ORDER BY hour")
    List<Object[]> findHourlyPostingPatternsRaw();

    @Query(nativeQuery = true, value =
            "SELECT DAYOFWEEK(created_at) as day, COUNT(*) as count " +
                    "FROM post " +
                    "GROUP BY DAYOFWEEK(created_at) " +
                    "ORDER BY day")
    List<Object[]> findDailyPostingPatternsRaw();

    @Query(nativeQuery = true, value =
            "SELECT WEEK(created_at) as week, COUNT(*) as count " +
                    "FROM post " +
                    "WHERE created_at >= :startOfYear " +
                    "GROUP BY WEEK(created_at) " +
                    "ORDER BY week")
    List<Object[]> findWeeklyPostingPatternsRaw(@Param("startOfYear") LocalDateTime startOfYear);
}