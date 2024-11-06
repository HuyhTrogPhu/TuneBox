package org.example.library.repository;

import org.example.library.model.Albums;
import org.example.library.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByCreatorId(Long userId);


    //    chuyen doi name thanh chu thường, khong phan biet hoa thuong, caác name or description có %keywword%
    @Query("SELECT p FROM Playlist p WHERE lower(p.title) LIKE lower(concat('%', :keywords, '%')) " +
            "OR lower(p.description) LIKE lower(concat('%', :keywords, '%'))")
    List<Playlist> searchByKeywords(@Param("keywords") String keywords);
}
