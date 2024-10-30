package org.example.library.repository;

import org.example.library.dto.GenreUserDto;
import org.example.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByName(String name);

    List<Genre> findAll();

    @Query("select new org.example.library.dto.GenreUserDto(ge.id, ge.name) from Genre ge")
    List<GenreUserDto> findNameGenre();

    // get list genre
    @Query("select g.name from Genre g join g.user u where u.id = :userId")
    List<String> findGenreByUserId(@Param("userId") Long userId);

}
