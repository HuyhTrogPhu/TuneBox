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
            "(SELECT b.blocker.id FROM Block b WHERE b.blocked.id = :currentUserId)")
    List<Post> findPostsExcludingBlockedUsers(@Param("currentUserId") Long currentUserId);
}
