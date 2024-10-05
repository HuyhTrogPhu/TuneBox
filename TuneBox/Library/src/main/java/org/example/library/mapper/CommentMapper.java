package org.example.library.mapper;

import org.example.library.dto.CommentDTO;
import org.example.library.model.Comment;
import org.example.library.model.User;
import org.example.library.model.Post;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // Chuyển từ Comment Entity sang CommentDTO
    public CommentDTO toDto(Comment comment) {
        if (comment == null) return null;

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreationDate(comment.getCreationDate());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setUserName(comment.getUser().getUserName());
        commentDTO.setPostId(comment.getPost().getId());

        return commentDTO;
    }

    // Chuyển từ CommentDTO sang Comment Entity
    public Comment toEntity(CommentDTO commentDTO, User user, Post post) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCreationDate(commentDTO.getCreationDate());
        comment.setUser(user);
        comment.setPost(post);

        // Set parentId nếu có
        if (commentDTO.getParentId() != null) {
            comment.setParentId(commentDTO.getParentId()); // Bạn cần thêm trường này trong lớp Comment entity
        }

        return comment;
    }

}
