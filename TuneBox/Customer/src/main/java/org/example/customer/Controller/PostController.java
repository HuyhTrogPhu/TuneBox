package org.example.customer.Controller;

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

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestParam(value = "content") String content, // Đảm bảo tên đúng
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        PostDto postDto = new PostDto();
        postDto.setContent(content); // Gán nội dung vào PostDto

        try {
            PostDto savedPost = postService.savePost(postDto, images);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id) {
        PostDto postDto = postService.getPostById(id);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }
    // Phương thức lấy tất cả các bài viết
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts(); // Tạo phương thức getAllPosts trong service
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}

