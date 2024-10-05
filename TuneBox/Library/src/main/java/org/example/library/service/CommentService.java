package org.example.library.service;

import org.example.library.dto.CommentDTO;
import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByPost(Long postId);
    CommentDTO addComment(Long postId, Long userId, CommentDTO commentDTO);
    void deleteComment(Long commentId);
}
