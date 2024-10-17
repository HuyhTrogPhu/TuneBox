package org.example.library.repository;

import org.example.library.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerIdAndFollowedId(Long followerId, Long followedId);

    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    int countByFollowedId(Long userId);

    int countByFollowerId(Long userId);

    List<Follow> findByFollowedId(Long userId);

    List<Follow> findByFollowerId(Long userId);
}