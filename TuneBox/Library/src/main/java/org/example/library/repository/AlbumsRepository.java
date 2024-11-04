package org.example.library.repository;

import org.example.library.model.Albums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumsRepository extends JpaRepository<Albums, Long> {
    List<Albums> findByCreatorId(Long userId);
    //    chuyen doi name thanh chu thường, khong phan biet hoa thuong, caác name or description có %keywword%
    @Query("SELECT a FROM Albums a WHERE lower(a.title) LIKE lower(concat('%', :keywords, '%')) " +
            "OR lower(a.description) LIKE lower(concat('%', :keywords, '%'))")
    List<Albums> searchByKeywords(@Param("keywords") String keywords);
    List<Albums> findAllByReportTrue();
}
