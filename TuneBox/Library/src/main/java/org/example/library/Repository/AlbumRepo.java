package org.example.library.Repository;

import org.example.library.model.Albums;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepo extends JpaRepository<Albums,Long> {

}
