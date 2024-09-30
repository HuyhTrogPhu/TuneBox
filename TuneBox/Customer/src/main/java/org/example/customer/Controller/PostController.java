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
            @RequestParam(value = "content") String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            HttpServletRequest request) {
        PostDto postDto = new PostDto();
        postDto.setContent(content);

        try {
            PostDto savedPost = postService.savePost(postDto, images, request); // Truyền request vào
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
        return postService.getPostsByUserId(userId); // Chỉ lấy bài viết của userId này
    }

    // Phương thức lấy tất cả các bài viết
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts(); // Tạo phương thức getAllPosts trong service
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


}
