package org.example.library.Repository;


import org.example.library.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepo extends JpaRepository<Track,Long> {

}
