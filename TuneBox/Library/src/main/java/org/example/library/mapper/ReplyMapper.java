package org.example.library.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.example.library.dto.ReplyDto;
import org.example.library.model.Reply;
import org.example.library.model.User;
import org.example.library.model.Comment;
import org.example.library.repository.CommentRepository;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class ReplyMapper {

    private final CommentRepository commentRepository;

    public ReplyMapper(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Chuyển từ Reply Entity sang ReplyDTO
    public ReplyDto toDto(Reply reply) {
        if (reply == null) return null;

        ReplyDto replyDTO = new ReplyDto();
        replyDTO.setId(reply.getId());
        replyDTO.setContent(reply.getContent());
        replyDTO.setCreationDate(reply.getCreationDate()); // Trực tiếp sử dụng LocalDateTime
        replyDTO.setUserId(reply.getUser().getId()); // ID của người reply
        replyDTO.setUserNickname(reply.getUser().getUserInformation().getName()); // Nickname của người reply
        replyDTO.setAvatar(reply.getUser().getUserInformation().getAvatar());
        // Lấy nickname của người bình luận gốc (người được reply)
        if (reply.getParentComment() != null && reply.getParentComment().getUser() != null) {
            replyDTO.setRepliedToNickname(reply.getParentComment().getUser().getUserInformation().getName()); // Gán nickname của người bình luận gốc
        }

        // Set commentId từ parentComment
        if (reply.getParentComment() != null) {
            replyDTO.setCommentId(reply.getParentComment().getId());
        }

        // Set parentReplyId từ parentReply
        if (reply.getParentReply() != null) {
            replyDTO.setParentReplyId(reply.getParentReply().getId());
        }

        return replyDTO;
    }

    // Chuyển từ ReplyDTO sang Reply Entity
    public Reply toEntity(ReplyDto replyDTO, User user, Long parentCommentId) {
        Reply reply = new Reply();
        reply.setContent(replyDTO.getContent());
        reply.setCreationDate(LocalDateTime.now());
        reply.setUser(user); // Assign user to the reply

        // If parentCommentId is provided, find the parent comment
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            reply.setParentComment(parentComment);
        }

        // If parentReplyId is provided, set the parent reply
        if (replyDTO.getParentReplyId() != null) {
            Reply parentReply = new Reply();
            parentReply.setId(replyDTO.getParentReplyId());
            reply.setParentReply(parentReply);
        }

        return reply;
    }
}
