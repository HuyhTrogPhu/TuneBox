package org.example.library.repository;

import org.example.library.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Report r WHERE r.user.id = :userId AND "
            + "((r.post.id = :postId AND :type = 'post' AND :postId IS NOT NULL) OR "
            + "(r.track.id = :trackId AND :type = 'track' AND :trackId IS NOT NULL) OR "
            + "(r.album.id = :albumId AND :type = 'album' AND :albumId IS NOT NULL))")
    boolean existsByUserIdAndType(
            @Param("userId") Long userId,
            @Param("postId") Long postId,
            @Param("trackId") Long trackId,
            @Param("albumId") Long albumId,
            @Param("type") String type
    );
        boolean existsByUserIdAndPostId(Long userId, Long postId);
        boolean existsByUserIdAndTrackId(Long userId, Long trackId);
        boolean existsByUserIdAndAlbumId(Long userId, Long albumId);
}
