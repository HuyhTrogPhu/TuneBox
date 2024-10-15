package org.example.library.repository;

import org.example.library.model.Post;
import org.example.library.model.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> findByCreatedAtAfter(LocalDateTime date);
    // Định nghĩa truy vấn cho findTopTrendingPosts
    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN p.likes l " +
            "LEFT JOIN p.comments c " +
            "GROUP BY p " +
            "ORDER BY (COUNT(l) + COUNT(c)) DESC " +
            "LIMIT 10")
    List<Post> findTopTrendingPosts();

    // Định nghĩa phương thức để lấy báo cáo
    @Query("SELECT pr FROM PostReport pr")
    List<PostReport> findAllReports();

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword%")
    List<Post> findByKeyword(@Param("keyword") String keyword);
}
