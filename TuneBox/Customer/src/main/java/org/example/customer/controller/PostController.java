package org.example.customer.controller;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;
import org.example.library.repository.LikeRepository;
import org.example.library.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    @Autowired
    private LikeRepository likeRepository;


    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "createdAt", required = false) String createdAt,
            @RequestParam("userId") Long userId) {
        PostDto postDto = new PostDto();
        postDto.setContent(content);
        try {
            // Kiểm tra nếu cả 'content' và 'images' đều trống
            if ((content == null || content.trim().isEmpty()) && (images == null || images.length == 0)) {
                throw new IllegalArgumentException("At least one image or content must be provided");
            }

            // Chuyển đổi chuỗi createdAt thành LocalDateTime nếu có
            if (createdAt != null && !createdAt.trim().isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(createdAt); // Chuyển đổi chuỗi thành LocalDateTime
                postDto.setCreatedAt(dateTime); // Gán giá trị cho createdAt
            } else {
                postDto.setCreatedAt(LocalDateTime.now()); // Nếu không có, sử dụng thời gian hiện tại
            }

            // Gọi service để lưu bài viết
            PostDto savedPost = postService.savePost(postDto, images, userId);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Xử lý lỗi định dạng thời gian
        }
    }


    // Lấy tất cả bài viết của người dùng từ ID
    @GetMapping("/current-user")
    public ResponseEntity<List<PostDto>> getPostsByCurrentUser(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Kiểm tra nếu userId không hợp lệ
        }

        List<PostDto> posts = postService.getPostsByUserId(userId);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Không có bài viết nào cho user này
        }

        return new ResponseEntity<>(posts, HttpStatus.OK); // Trả về danh sách bài viết
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<Void> changePostVisibility(@PathVariable Long id, @RequestParam("hidden") boolean hidden) {
        try {
            postService.changePostVisibility(id, hidden);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Phương thức lấy tất cả các bài viết
    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam Long currentUserId) {
        List<PostDto> posts = postService.getAllPosts(currentUserId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Phương thức cập nhật bài viết
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long id,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "createdAt", required = false) String createdAt, // Thêm trường createdAt
            @RequestParam("userId") Long userId) {

        PostDto postDto = new PostDto();
        postDto.setId(id);
        postDto.setContent(content); // Nội dung có thể là null

        try {
            // Kiểm tra nếu cả 'content' và 'images' đều không có
            if ((content == null || content.trim().isEmpty()) && (images == null || images.length == 0)) {
                throw new IllegalArgumentException("At least one image or content must be provided");
            }

            // Chuyển đổi chuỗi createdAt thành LocalDateTime nếu có
            if (createdAt != null && !createdAt.trim().isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(createdAt); // Chuyển đổi chuỗi thành LocalDateTime
                postDto.setCreatedAt(dateTime); // Gán giá trị cho createdAt
            }

            // Gọi service để cập nhật bài post
            PostDto updatedPost = postService.updatePost(postDto, images, userId);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Xử lý lỗi định dạng thời gian
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
    // Lấy tất cả bài viết của người dùng từ ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getPostsByUserId(@PathVariable Long userId) {
        // Kiểm tra nếu userId không hợp lệ
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Lấy danh sách bài viết
        List<PostDto> posts = postService.getPostsByUserId(userId);
        System.out.println("Posts for user ID " + userId + ": " + posts); // Log các bài viết

        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


}