package org.example.customer.Controller;

import org.example.library.dto.CommentDTO;
import org.example.library.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Lấy tất cả các comment của một bài viết
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    // Thêm comment vào bài viết
    @PostMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @PathVariable Long userId,
                                                 @RequestParam(value = "createdAt", required = false) String createdAt,
                                                 @RequestBody CommentDTO commentDTO) {
        // Nếu createdAt được truyền từ request, chuyển đổi nó sang LocalDateTime
        if (createdAt != null && !createdAt.isEmpty()) {
            commentDTO.setCreationDate(LocalDateTime.parse(createdAt));
        }

        CommentDTO createdComment = commentService.addComment(postId, userId, commentDTO);
        return ResponseEntity.ok(createdComment);
    }


    // Xóa comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // Cập nhật comment
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }


}
