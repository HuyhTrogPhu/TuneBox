package org.example.library.repository;

import org.example.library.model.Albums;
import org.example.library.model.Playlist;
import org.example.library.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByCreatorId(Long userId);

    //    chuyen doi name thanh chu thường, khong phan biet hoa thuong, caác name or description có %keywword%
    @Query("SELECT p FROM Playlist p WHERE lower(p.title) LIKE lower(concat('%', :keywords, '%')) " +
            "OR lower(p.description) LIKE lower(concat('%', :keywords, '%'))")
    List<Playlist> searchByKeywords(@Param("keywords") String keywords);
    Long countByCreateDateBetween(LocalDate startDate, LocalDate endDate);
    Long countByCreateDate(LocalDate currentDate);
    List<Playlist> findAllByCreateDateBetween(LocalDate startDate, LocalDate endDate);
}
