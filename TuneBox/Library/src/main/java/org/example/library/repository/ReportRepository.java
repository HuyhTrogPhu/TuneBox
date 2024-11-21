package org.example.library.repository;

import org.example.library.model.Post;
import org.example.library.model.Report;
import org.example.library.model_enum.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

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

    List<Report> findAll(); // Truy xuất tất cả các báo cáo


    List<Report> findByStatus(ReportStatus status); // Phương thức này đã có
    List<Report> findByPost(Post post);
    List<Report> findByPostIdAndStatus(Long postId, ReportStatus status);

    List<Report> findByStatusAndType(ReportStatus status, String type);


    @Query("SELECT r FROM Report r WHERE r.status = :status AND r.createDate BETWEEN :startDate AND :endDate")
    List<Report> findByStatusAndDateRange(
            @Param("status") ReportStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT r FROM Report r WHERE r.status = :status AND r.createDate = :specificDate")
    List<Report> findByStatusAndSpecificDate(
            @Param("status") ReportStatus status,
            @Param("specificDate") LocalDate specificDate
    );


    @Query("SELECT r FROM Report r JOIN r.track t WHERE t IS NOT NULL")
    Page<Report> findAllReportsWithTracks(Pageable pageable);

    @Query("SELECT r FROM Report r JOIN r.album t WHERE t IS NOT NULL")
    Page<Report> findAllReportsWithAlbum(Pageable pageable);


    @Query("SELECT r FROM Report r JOIN r.post t WHERE t IS NOT NULL")
    Page<Report> findAllReportsWithPost(Pageable pageable);

    List<Report>findAllByTrackId(Long trackId);
    List<Report>findAllByAlbumId(Long albumId);
}
