package org.example.customer.controller;

import org.example.library.dto.ReplyDto;
import org.example.library.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/comment/{commentId}/user/{userId}")
    public ResponseEntity<ReplyDto> addReply(@PathVariable Long commentId,
                                             @PathVariable Long userId,
                                             @RequestBody ReplyDto replyDto,
                                             @RequestParam(required = false) Long parentReplyId) {
        System.out.println("Comment ID: " + commentId);
        System.out.println("User ID: " + userId);
        System.out.println("Reply DTO: " + replyDto);

        ReplyDto createdReply = replyService.addReply(commentId, userId, replyDto, parentReplyId);
        return new ResponseEntity<>(createdReply, HttpStatus.CREATED);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<ReplyDto>> getRepliesByComment(@PathVariable Long commentId) {
        List<ReplyDto> replies = replyService.getRepliesByComment(commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/reply/{parentReplyId}/user/{userId}")
    public ResponseEntity<ReplyDto> addReplyToReply(
            @PathVariable Long parentReplyId,
            @PathVariable Long userId,
            @RequestBody ReplyDto replyDto) {

        ReplyDto newReply = replyService.addReplyToReply(parentReplyId, userId, replyDto);
        return new ResponseEntity<>(newReply, HttpStatus.CREATED);
    }

}
