package org.example.library.repository;

import org.example.library.model.Genre;
import org.example.library.model.Talent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;


public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByName(String name);
}
