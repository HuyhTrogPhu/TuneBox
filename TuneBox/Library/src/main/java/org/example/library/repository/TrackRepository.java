package org.example.library.repository;

import org.example.library.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByCreatorId(Long userId);
}
