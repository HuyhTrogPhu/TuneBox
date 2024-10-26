package org.example.customer.controller;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;
import org.example.library.repository.LikeRepository;
import org.example.library.service.NotificationService;
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
    private final NotificationService notificationService;

    @Autowired
    private LikeRepository likeRepository;

    public PostController(PostService postService, NotificationService notificationService) {
        this.postService = postService;
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "createdAt", required = false) String createdAt,
            @RequestParam("userId") Long userId) {

        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Kiểm tra nếu userId không hợp lệ
        }

        PostDto postDto = new PostDto();
        postDto.setContent(content);
        try {
            // Kiểm tra nếu cả 'content' và 'images' đều trống
            if ((content == null || content.trim().isEmpty()) && (images == null || images.length == 0)) {
                throw new IllegalArgumentException("At least one image or content must be provided");
            }

            // Chuyển đổi chuỗi createdAt thành LocalDateTime nếu có
            if (createdAt != null && !createdAt.trim().isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(createdAt);
                postDto.setCreatedAt(dateTime);
            } else {
                postDto.setCreatedAt(LocalDateTime.now());
            }

            // Gọi service để lưu bài viết
            PostDto savedPost = postService.savePost(postDto, images, userId);

            // Gửi thông báo đến người theo dõi
            notificationService.notifyFollowersOfNewPost(savedPost.getId(), userId);

            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
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
            @RequestParam(value = "createdAt", required = false) String createdAt,
            @RequestParam("userId") Long userId) {

        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Kiểm tra nếu userId không hợp lệ
        }

        PostDto postDto = new PostDto();
        postDto.setId(id);
        postDto.setContent(content);

        try {
            if ((content == null || content.trim().isEmpty()) && (images == null || images.length == 0)) {
                throw new IllegalArgumentException("At least one image or content must be provided");
            }

            if (createdAt != null && !createdAt.trim().isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(createdAt);
                postDto.setCreatedAt(dateTime);
            }

            PostDto updatedPost = postService.updatePost(postDto, images, userId);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<PostDto> posts = postService.getPostsByUserId(userId);
        System.out.println("Posts for user ID " + userId + ": " + posts);

        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDto> getPostByPostId(@PathVariable Long postId) {
        // Kiểm tra nếu postId là null
        if (postId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Gọi service để lấy bài viết theo postId
        PostDto postDto = postService.getPostByPostId(postId);

        // Kiểm tra xem bài viết có tồn tại không
        if (postDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Nếu không tìm thấy bài viết
        }

        return new ResponseEntity<>(postDto, HttpStatus.OK); // Trả về bài viết với trạng thái 200 OK
    }

}
