package org.example.library.repository;

import org.example.library.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
    boolean existsByUserAndTrack(User user, Track track);
    boolean existsByUserAndPlaylist(User user, Playlist playlist);

    Optional<Like> findByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndTrack(User user, Track track);
    Optional<Like> findByUserAndPlaylist(User user, Playlist playlist);

    List<Like> findByPostId(Long postId);
    List<Like> findByTrackId(Long trackId);

    long countByPostId(Long postId);
    long countByTrackId(Long trackId);
    long countByplaylistId(Long playlistId);

    List<Like> findByUserId(Long userId);
}