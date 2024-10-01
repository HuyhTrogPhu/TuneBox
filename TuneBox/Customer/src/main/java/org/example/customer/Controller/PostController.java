package org.example.customer.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.library.dto.PostDto;
import org.example.library.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("/*")
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestParam(value = "content", required = false) String content, // Chỉ cần thay đổi 'required' thành false
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            HttpServletRequest request) {
        PostDto postDto = new PostDto();
        postDto.setContent(content); // Nội dung có thể là null

        try {
            // Kiểm tra có ít nhất một hình ảnh hoặc có nội dung không
            if (images == null || images.length == 0) {
                throw new IllegalArgumentException("At least one image or content must be provided");
            }

            PostDto savedPost = postService.savePost(postDto, images, request);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // Lấy tất cả bài viết của người dùng từ session
    @GetMapping("/current-user")
    public List<PostDto> getPostsByCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new RuntimeException("User not logged in");
        }
        Long userId = (Long) session.getAttribute("userId");
        return postService.getPostsByUserId(userId);
    }

    // Phương thức lấy tất cả các bài viết
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Phương thức cập nhật bài viết
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long id,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            HttpServletRequest request) {
        PostDto postDto = new PostDto();
        postDto.setId(id);
        postDto.setContent(content);

        try {
            PostDto updatedPost = postService.updatePost(postDto, images, request);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Hoặc sử dụng status khác tùy thuộc vào lỗi
        }
    }

    // Phương thức xóa bài viết
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
