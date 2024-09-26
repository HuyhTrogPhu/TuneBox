package org.example.customer.Controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.PostDto;
import org.example.library.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("API")
public class PostController {
    private PostService postService;

    //create Post
    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(
            @RequestPart("postDto") PostDto postDto,
            @RequestPart("images") MultipartFile[] images) {
        PostDto savedPost = postService.savePost(postDto, images);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }
    //get Post
    @GetMapping("/get")
    public ResponseEntity<PostDto> getPost(Model model, @RequestParam("id") Long id) {
        List<PostDto> postList = postService.getAllPosts();
        model.addAttribute("postList", postList);
        return new ResponseEntity<>(postList.get(0), HttpStatus.OK);
    }

}
