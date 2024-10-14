package org.example.library.mapper;

import org.example.library.dto.CommentDTO;
import org.example.library.model.Comment;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.model.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
        commentDTO.setUserNickname(comment.getUser().getUserNickname());
        commentDTO.setPostId(comment.getPost().getId());
        commentDTO.setEdited(comment.isEdited());

        return commentDTO;
    }

    // Chuyển từ CommentEntity sang CommentDTO, theo dõi thông tin bài hát
    public CommentDTO toDto_Track(Comment comment) {
        if (comment == null) return null;

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreationDate(comment.getCreationDate());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setUserNickname(comment.getUser().getUserNickname());
        commentDTO.setEdited(comment.isEdited());

        commentDTO.setTrackId(comment.getTrack().getId());

        return commentDTO;
    }



    // Chuyển từ CommentDTO sang Comment Entity
    public Comment toEntity(CommentDTO commentDTO, User user, Post post) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());

        // Nếu creationDate từ DTO là null, đặt giá trị mặc định là thời gian hiện tại
        if (commentDTO.getCreationDate() == null) {
            comment.setCreationDate(LocalDateTime.now());
        } else {
            comment.setCreationDate(commentDTO.getCreationDate());
        }

        comment.setUser(user);
        comment.setPost(post);
        comment.setEdited(commentDTO.isEdited());

        if (commentDTO.getParentId() != null) {
            comment.setParentId(commentDTO.getParentId());
        }

        return comment;
    }


    // Chuyển từ CommentDTO sang Comment Entity
    public Comment toEntity_Track(CommentDTO commentDTO, User user, Track track) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());

        // Nếu creationDate từ DTO là null, đặt giá trị mặc định là thời gian hiện tại
        if (commentDTO.getCreationDate() == null) {
            comment.setCreationDate(LocalDateTime.now());
        } else {
            comment.setCreationDate(commentDTO.getCreationDate());
        }

        comment.setUser(user);
        comment.setTrack(track);
        comment.setEdited(commentDTO.isEdited());

        if (commentDTO.getParentId() != null) {
            comment.setParentId(commentDTO.getParentId());
        }

        return comment;
    }




}
