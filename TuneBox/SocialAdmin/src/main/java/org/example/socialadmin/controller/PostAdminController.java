package org.example.socialadmin.controller;

import org.example.library.Exception.PostNotFoundException;
import org.example.library.dto.PostDto;
import org.example.library.dto.ReportDto;
import org.example.library.service.PostService; // Giả định bạn đã có service cho Post
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/posts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PostAdminController {

    @Autowired
    private PostService postService; // Tiêm PostService

    //search all bai viet
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        try {
            List<PostDto> posts = postService.findAllPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/new")
    public ResponseEntity<List<PostDto>> getNewPosts() {
        try {
            List<PostDto> newPosts = postService.findNewPosts(); // Giả định có phương thức lấy bài mới
            return ResponseEntity.ok(newPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalPosts() {
        try {
            long totalPosts = postService.countTotalPosts(); // Gọi phương thức để lấy tổng số bài viết
            return ResponseEntity.ok(totalPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/report")
    public List<ReportDto> getPendingReports() {
        return postService.getReportedPosts();
    }


    //search theo id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        try {
            PostDto post = postService.findPostById(id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //update post
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostDto postDto,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws IOException {
        try {
            postDto.setId(id);
            Long userId = postDto.getUserId();
            PostDto updatedPost = postService.updatePost(postDto, images, userId);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //xoa bai viet
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (PostNotFoundException e) {  // Ngoại lệ cụ thể khi không tìm thấy bài viết
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting post");
        }
    }

    //xu ly bai viet

}
