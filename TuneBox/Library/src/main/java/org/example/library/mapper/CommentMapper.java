package org.example.library.mapper;

import org.example.library.dto.BrandsDto;
import org.example.library.dto.CommentDto;
import org.example.library.model.Brand;
import org.example.library.model.Comment;

public class CommentMapper {

    public static CommentDto maptoCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContent(), comment.getCreationDate(), comment.getCommentLikes(), comment.getTrack(), comment.getPost(), comment.getUser());
    }

    public static Comment maptoComment(CommentDto commentDto) {
        return new Comment(commentDto.getId(), commentDto.getContent(), commentDto.getCreationDate(), commentDto.getCommentLikes(), commentDto.getTrack(), commentDto.getPost(), commentDto.getUser());
    }

}
