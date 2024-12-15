package org.example.library.repository;

import org.example.library.dto.PostReactionDto;
import org.example.library.model.Post;
import org.example.library.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    @Query("SELECT p FROM Post p WHERE p.user.id NOT IN " +
            "(SELECT b.blocked.id FROM Block b WHERE b.blocker.id = :currentUserId) " +
            "AND p.user.id NOT IN " +
            "(SELECT b.blocker.id FROM Block b WHERE b.blocked.id = :currentUserId) " +
            "AND p.hidden = false " +
            "AND p.user.status = 'ACTIVE'") // Lọc bài viết từ người dùng có trạng thái ACTIVE
    List<Post> findPostsExcludingBlockedUsers(@Param("currentUserId") Long currentUserId);



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
    @Query("SELECT pr FROM Report pr")
    List<Report> findAllReports();

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword%")
    List<Post> findByKeyword(@Param("keyword") String keyword);
    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    List<Post> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Sort sort);

    List<Post> findByUserIdAndHidden(Long userId, boolean isHidden);

    @Query("SELECT p FROM Post p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate")
    Page<Post> findAllByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);


    List<Post> findByUser_IdAndAdminPermanentlyHiddenFalse(Long userId);


    List<Post> findByAdminHiddenTrue();


}
