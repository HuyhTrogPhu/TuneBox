package org.example.customer.Controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.PostDto;
import org.example.library.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class PostController {
    private PostService postService;

    //create Post
    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestPart("postDto") PostDto postDto,
            @RequestPart("images") MultipartFile[] images) {
        PostDto savedPost = postService.savePost(postDto, images);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

}
