package org.example.library.Repository;


import org.example.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepo extends JpaRepository<Genre,Long> {

}
