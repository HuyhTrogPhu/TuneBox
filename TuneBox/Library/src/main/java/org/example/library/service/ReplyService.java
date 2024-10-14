package org.example.library.service;

import org.example.library.dto.CommentDTO;
import org.example.library.dto.ReplyDto;

import java.util.List;

public interface ReplyService {

    ReplyDto addReply(Long commentId, Long userId, ReplyDto replyDto, Long parentReplyId);

    ReplyDto addReplyToReply(Long parentReplyId, Long userId, ReplyDto replyDto);

    List<ReplyDto> getRepliesByComment(Long commentId);
    void deleteReply(Long replyId);

    ReplyDto updateReply(Long userId, ReplyDto replyDto);
}
