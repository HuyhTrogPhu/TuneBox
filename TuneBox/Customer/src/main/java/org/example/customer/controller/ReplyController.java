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
        System.out.println("User Nickname: " + replyDto.getUserNickname());
        System.out.println("Reply DTO: " + replyDto);

        ReplyDto createdReply = replyService.addReply(commentId, userId, replyDto, parentReplyId);
        return new ResponseEntity<>(createdReply, HttpStatus.CREATED);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<ReplyDto>> getRepliesByComment(@PathVariable Long commentId) {
        List<ReplyDto> replies = replyService.getRepliesByComment(commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @DeleteMapping("/reply/{replyId}/user/{userId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId, @PathVariable Long userId) {
        replyService.deleteReply(replyId, userId);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content nếu xóa thành công
    }

    @PostMapping("/reply/{parentReplyId}/user/{userId}")
    public ResponseEntity<ReplyDto> addReplyToReply(
            @PathVariable Long parentReplyId,
            @PathVariable Long userId,
            @RequestBody ReplyDto replyDto) {

        // Lấy commentId từ replyDto hoặc có thể từ thông tin nào đó khác nếu cần
        Long commentId = replyDto.getCommentId(); // Đảm bảo rằng ReplyDto có trường commentId

        // Gọi service để thêm reply cho reply gốc
        ReplyDto newReply = replyService.addReplyToReply(parentReplyId, userId, replyDto, commentId);
        return new ResponseEntity<>(newReply, HttpStatus.CREATED);
    }

    @PutMapping("/reply/{replyId}/user/{userId}")
    public ResponseEntity<ReplyDto> updateReply(
            @PathVariable Long replyId,
            @PathVariable Long userId,
            @RequestBody ReplyDto replyDto) {
        ReplyDto updatedReply = replyService.updateReply(replyId, userId, replyDto);
        return ResponseEntity.ok(updatedReply);
    }
}
