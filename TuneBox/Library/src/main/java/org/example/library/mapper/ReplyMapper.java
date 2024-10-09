package org.example.library.mapper;

import org.example.library.dto.ReplyDto;
import org.example.library.model.Reply;
import org.example.library.model.User;
import org.example.library.model.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReplyMapper {

    // Chuyển từ Reply Entity sang ReplyDTO
    public ReplyDto toDto(Reply reply) {
        if (reply == null) return null;

        ReplyDto replyDTO = new ReplyDto();
        replyDTO.setId(reply.getId());
        replyDTO.setContent(reply.getContent());
        replyDTO.setCreationDate(reply.getCreationDate()); // Trực tiếp sử dụng LocalDateTime
        replyDTO.setUserId(reply.getUserId());

        // Lấy tên người dùng từ thông tin User nếu cần
        if (reply.getParentComment() != null && reply.getParentComment().getUser() != null) {
            replyDTO.setUserName(reply.getParentComment().getUser().getUserName());
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
    public Reply toEntity(ReplyDto replyDTO, User user, Comment parentComment) {
        Reply reply = new Reply();
        reply.setContent(replyDTO.getContent());
        reply.setCreationDate(LocalDateTime.now()); // Sử dụng LocalDateTime trực tiếp
        reply.setUserId(user.getId());
        reply.setParentComment(parentComment); // Thiết lập bình luận cha cho reply

        // Nếu có parentReplyId thì thiết lập parentReply
        if (replyDTO.getParentReplyId() != null) {
            Reply parentReply = new Reply();
            parentReply.setId(replyDTO.getParentReplyId());
            reply.setParentReply(parentReply); // Thiết lập reply cha cho reply
        }

        return reply;
    }
}
