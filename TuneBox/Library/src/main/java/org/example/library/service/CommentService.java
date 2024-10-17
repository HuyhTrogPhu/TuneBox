package org.example.library.service;

import org.example.library.dto.CommentDTO;
import java.util.List;

public interface CommentService {

    // Post comments
    List<CommentDTO> getCommentsByPost(Long postId);
    CommentDTO addComment(Long postId, Long userId, CommentDTO commentDTO);
    void deleteComment(Long commentId);
    CommentDTO updateComment(Long commentId, CommentDTO commentDTO);

    // Track comments
    CommentDTO addCommentTrack(Long trackId, Long userId, CommentDTO commentDTO);
    void deleteCommentTrack(Long commentId);
    CommentDTO updateCommentTrack(Long commentId, CommentDTO commentDTO);
    List<CommentDTO> getCommentsByTrack(Long trackId);
}
