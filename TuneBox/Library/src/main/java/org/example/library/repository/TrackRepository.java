package org.example.library.repository;

import org.example.library.dto.TrackDto;
import org.example.library.model.Brand;
import org.example.library.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByCreatorId(Long userId);
    List<Track> findByGenreId(Long genreId);

//    chuyen doi name thanh chu thường, khong phan biet hoa thuong, caác name or description có %keywword%
    @Query("SELECT t FROM Track t WHERE lower(t.name) LIKE lower(concat('%', :keywords, '%')) " +
            "OR lower(t.description) LIKE lower(concat('%', :keywords, '%'))")
    List<Track> searchByKeywords(@Param("keywords") String keywords);

}
