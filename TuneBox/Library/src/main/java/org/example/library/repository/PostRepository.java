package org.example.library.repository;

import org.example.library.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    @Query("SELECT p FROM Post p WHERE p.user.id NOT IN " +
            "(SELECT b.blocked.id FROM Block b WHERE b.blocker.id = :currentUserId) " +
            "AND p.user.id NOT IN " +
            "(SELECT b.blocker.id FROM Block b WHERE b.blocked.id = :currentUserId) " +
            "AND p.hidden = false") // Thêm điều kiện này để lọc các bài viết bị ẩn
    List<Post> findPostsExcludingBlockedUsers(@Param("currentUserId") Long currentUserId);

//    @Query("SELECT p FROM Post p JOIN Report r ON p.id = r.post.id WHERE r.status = 'PENDING'")
//    List<Post> findReportedPosts(); // Truy vấn các bài đăng có báo cáo đang chờ xử lý


}
