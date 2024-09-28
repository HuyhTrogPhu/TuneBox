package org.example.library.repository;

import org.example.library.model.Comment;
import org.example.library.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
