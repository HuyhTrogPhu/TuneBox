package org.example.library.Repository;


import org.example.library.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepo extends JpaRepository<Playlist,Long> {

}
