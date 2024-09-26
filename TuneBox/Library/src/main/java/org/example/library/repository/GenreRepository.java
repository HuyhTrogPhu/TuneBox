package org.example.library.repository;

import org.example.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;


public interface GenreRepository extends JpaRepository<Genre, Long> {

}
