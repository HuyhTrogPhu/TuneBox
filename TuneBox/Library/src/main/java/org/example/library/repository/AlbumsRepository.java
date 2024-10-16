package org.example.library.repository;

import org.example.library.model.Albums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumsRepository extends JpaRepository<Albums, Long> {
    List<Albums>findByCreatorId(Long userId);
}
