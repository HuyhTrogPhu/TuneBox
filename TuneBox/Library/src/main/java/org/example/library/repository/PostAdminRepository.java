package org.example.library.repository;

import org.example.library.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostAdminRepository extends JpaRepository<Post, Long> {
    // Tìm bài đăng có nhiều lượt tương tác
    @Query("SELECT p FROM Post p LEFT JOIN p.likes l LEFT JOIN p.comments c WHERE p.hidden = false GROUP BY p ORDER BY COUNT(l) + COUNT(c) DESC")
    List<Post> findTopPostsByInteractions();

    // Tìm các bài đăng có ảnh
    @Query("SELECT p FROM Post p WHERE p.images IS NOT EMPTY ORDER BY p.createdAt DESC")
    List<Post> findPostsWithImages();

    // Tìm các bài đăng không có ảnh
    @Query("SELECT p FROM Post p WHERE p.images IS EMPTY ORDER BY p.createdAt DESC")
    List<Post> findPostsWithoutImages();

    // Tìm các bài đăng mới nhất (sắp xếp theo ngày tạo giảm dần)
    List<Post> findAllByOrderByCreatedAtDesc();

    //
}
