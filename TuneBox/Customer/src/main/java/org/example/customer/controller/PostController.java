package org.example.customer.controller;

import org.example.library.dto.PostDto;
import org.example.library.dto.UserTag;
import org.example.library.model.Post;
import org.example.library.repository.LikeRepository;
import org.example.library.service.NotificationService;
import org.example.library.service.PostService;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final NotificationService notificationService;

    @Autowired
    private UserService userService;

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
            @RequestParam(value = "IdShare", required = false) String IdShare,
            @RequestParam("userId") Long userId) {

        Long idShareLong = null;
        if (IdShare != null && !IdShare.isEmpty()) {
            idShareLong = Long.parseLong(IdShare);
        }

        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Kiểm tra nếu userId không hợp lệ
        }

        PostDto postDto = new PostDto();
        postDto.setContent(content);
        postDto.setIdShare(idShareLong);
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

      @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam Long currentUserId) {
        try {
            List<PostDto> posts = postService.getAllPosts(currentUserId);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching posts: " + e.getMessage());
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<List<PostDto>> getUserPosts(@PathVariable Long userId, Principal principal) {

        userService.checkAccountStatus(userId);

        String currentUsername = principal.getName();
        List<PostDto> posts = postService.getPostsByUserId(userId, currentUsername);
        return ResponseEntity.ok(posts);
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

        // Kiểm tra xem bài viết có bị khóa bởi admin hay không
        Post post = postService.findPostById(postId);
        if (post.isAdminHidden() || post.isAdminPermanentlyHidden()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Nếu bài viết bị khóa, trả về 403 Forbidden
        }

        return new ResponseEntity<>(postDto, HttpStatus.OK); // Trả về bài viết với trạng thái 200 OK
    }


    // ẩn bài viết
    @PutMapping("/{postId}/toggle-visibility")
    public ResponseEntity<Map<String, Object>> toggleVisibility(@PathVariable Long postId, Principal principal) {
        Post post = postService.findPostById(postId);
        System.out.println("Người dùng: " + principal.getName() + " đang cố gắng thay đổi trạng thái bài viết: " + postId);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Trả về null nếu bài viết không tồn tại
        }

        // Kiểm tra nếu bài viết đã bị khóa bởi admin
        if (post.isAdminPermanentlyHidden()) {
            throw new IllegalStateException("Post is permanently hidden by admin");
        }

        // Kiểm tra quyền truy cập
        if (!postService.userCanToggleHidden(postId, principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Trả về null nếu không có quyền
        }

        // Chuyển đổi trạng thái hidden
        boolean newVisibility = !post.isHidden();
        post.setHidden(newVisibility);
        postService.save(post); // Lưu lại thay đổi

        Map<String, Object> response = new HashMap<>();
        response.put("message", newVisibility ? "Bài viết đã được ẩn." : "Bài viết đã được hiện.");
        response.put("isHidden", newVisibility);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tagName")
    public ResponseEntity<List<UserTag>> getPostsByTagName(@RequestParam("userId") Long userId) {
        try {
            List<UserTag> userTag = userService.getUserTags(userId);
            return ResponseEntity.ok(userTag);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}