package org.example.customer.Controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.PostDto;
import org.example.library.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class PostController {
    private PostService postService;

    //create Post
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, MultipartFile image) {
        PostDto savedPost = postService.savePost(postDto,image);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

}
