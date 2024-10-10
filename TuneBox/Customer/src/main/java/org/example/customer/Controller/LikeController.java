package org.example.customer.Controller;

import org.example.library.dto.LikeDto;
import org.example.library.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins ="http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/add")
    public ResponseEntity<LikeDto> addLike(@RequestParam Long userId, @RequestParam Long postId) {
        LikeDto likeDto = likeService.addLike(userId, postId);
        return ResponseEntity.ok(likeDto);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeLike(@RequestParam Long userId, @RequestParam Long postId) {
        likeService.removeLike(userId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<LikeDto>> getLikesByPostId(@PathVariable Long postId) {
        List<LikeDto> likes = likeService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }
    @GetMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<Boolean> checkUserLike(@PathVariable Long postId, @PathVariable Long userId) {
        boolean hasLiked = likeService.checkUserLike(postId, userId);
        return ResponseEntity.ok(hasLiked);
    }

}

