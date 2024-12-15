package org.example.library.repository;

import org.example.library.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerIdAndFollowedId(Long followerId, Long followedId);

    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    int countByFollowedId(Long userId);

    int countByFollowerId(Long userId);

    @Query("SELECT f FROM Follow f WHERE f.followed.id = :userId AND f.followed.status = 'ACTIVE'")
    List<Follow> findByFollowedId(@Param("userId") Long userId);

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :userId AND f.follower.status = 'ACTIVE'")
    List<Follow> findByFollowerId(@Param("userId")Long userId);

    // Lấy số lượng follower của một user
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followed.id = :userId")
    Long countFollowersByUserId(@Param("userId") Long userId);

    // Lấy số lượng following của một user
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
    Long countFollowingByUserId(@Param("userId") Long userId);

}