package org.example.library.repository;

import org.example.library.model.Albums;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumsRepository extends JpaRepository<Albums, Long> {
}
