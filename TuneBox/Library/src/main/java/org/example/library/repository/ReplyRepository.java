package org.example.library.repository;

import org.example.library.model.Comment;
import org.example.library.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByParentComment(Comment parentComment); // Sửa lại phương thức

}
