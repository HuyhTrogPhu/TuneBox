package org.example.library.repository;

import org.example.library.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostAdminRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p LEFT JOIN p.likes l LEFT JOIN p.comments c WHERE p.hidden = false GROUP BY p ORDER BY COUNT(l) + COUNT(c) DESC")
    List<Post> findTopPostsByInteractions();

    @Query("SELECT p FROM Post p WHERE p.images IS NOT EMPTY ORDER BY p.createdAt DESC")
    List<Post> findPostsWithImages();

    @Query("SELECT p FROM Post p WHERE p.images IS EMPTY ORDER BY p.createdAt DESC")
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

    @Query("SELECT COUNT(p) FROM Post p WHERE p.images IS NOT EMPTY")
    Long countPostsWithImages();

    @Query("SELECT COALESCE(AVG(SIZE(p.likes)), 0.0) FROM Post p")
    Double calculateAverageLikesPerPost();

    @Query(value = """
    SELECT DATE(p.created_at) as date,
           COUNT(DISTINCT p.id) as postCount,
           COUNT(DISTINCT l.id) as likeCount,
           COUNT(DISTINCT c.id) as commentCount,
           CASE 
               WHEN COUNT(DISTINCT p.id) > 0 
               THEN (COUNT(DISTINCT l.id) + COUNT(DISTINCT c.id)) * 100.0 / COUNT(DISTINCT p.id)
               ELSE 0.0 
           END as interactionPercentage
    FROM post p
    LEFT JOIN likes l ON l.post_id = p.id
    LEFT JOIN comment c ON c.post_id = p.id
    WHERE p.created_at >= :startDate
    GROUP BY DATE(p.created_at)
    ORDER BY DATE(p.created_at)
    """, nativeQuery = true)
    List<Object[]> findDailyPostStatsRaw(@Param("startDate") LocalDateTime startDate);

}