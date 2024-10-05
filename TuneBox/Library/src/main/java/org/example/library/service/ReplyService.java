package org.example.library.service;

import org.example.library.dto.ReplyDto;

import java.util.List;

public interface ReplyService {
    ReplyDto addReply(Long commentId, Long userId, ReplyDto replyDTO);
    List<ReplyDto> getRepliesByComment(Long commentId);
    void deleteReply(Long replyId);
}
