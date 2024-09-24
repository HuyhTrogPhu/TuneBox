package org.example.library.repository;

import org.example.library.model.PostVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostVideoRepository extends JpaRepository<PostVideo, Long> {
}
