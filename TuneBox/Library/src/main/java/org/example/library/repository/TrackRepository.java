package org.example.library.repository;


import org.example.library.model.Track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByCreatorId(Long userId);
    List<Track> findByGenreId(Long genreId);

//    chuyen doi name thanh chu thường, khong phan biet hoa thuong, caác name or description có %keywword%
    @Query("SELECT t FROM Track t WHERE lower(t.name) LIKE lower(concat('%', :keywords, '%')) " +
            "OR lower(t.description) LIKE lower(concat('%', :keywords, '%'))")
    List<Track> searchByKeywords(@Param("keywords") String keywords);

    // Lay Track theo ID cua Album
    @Query("SELECT a.tracks FROM Albums a WHERE a.id = :albumId")
    List<Track> findTracksByAlbumId(@Param("albumId") Long albumId);
    List<Track> findByReportTrue();


    Long countByCreateDate(LocalDateTime createDate);
    Long countByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT t.genre.name AS genre, COUNT(t) AS trackCount " +
            "FROM Track t " +
            "WHERE t.createDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.genre.name")
    List<Object[]> countTracksByGenreAndDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

 List<Track> findAllByCreateDateBetween(LocalDateTime startDate,LocalDateTime endDate);
 List<Track> findAllByPlaylistsId(@Param("playlistsId") long playlistId);
 List<Track> findAllByAlbumsId(@Param("playlistsId") long playlistId);

}
