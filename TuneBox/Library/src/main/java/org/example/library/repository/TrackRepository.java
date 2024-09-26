package org.example.library.repository;

import org.example.library.model.Brand;
import org.example.library.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
