package org.example.library.repository;

import org.example.library.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByTrackId(Long trackId);
    long countByTrackId(Long trackId);
    long countByPostId(Long postId);
    long countByUserId(Long userId);


}
