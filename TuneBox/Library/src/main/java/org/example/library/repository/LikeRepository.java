package org.example.library.repository;

import org.example.library.model.Like;
import org.example.library.model.Post;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndPost(User user, Post post);
    List<Like> findByPostId(Long postId);
}
