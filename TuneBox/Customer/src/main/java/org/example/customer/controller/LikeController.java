package org.example.customer.controller;

import org.example.library.dto.LikeDto;
import org.example.library.dto.TrackDto;
import org.example.library.model.Like;
import org.example.library.model.Post;
import org.example.library.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<LikeDto> addLike(@RequestBody LikeDto likeDto) {
        try {
            // Kiểm tra nếu likeDto không hợp lệ
            if (likeDto.getUserId() == null || (likeDto.getPostId() == null && likeDto.getTrackId() == null)) {
                return ResponseEntity.badRequest().body(null);
            }

            // Thêm like
            LikeDto createdLikeDto = likeService.addLike(likeDto.getUserId(), likeDto.getPostId(), likeDto.getTrackId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLikeDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeLike(@RequestParam Long userId,
                                           @RequestParam(required = false) Long postId,
                                           @RequestParam(required = false) Long trackId) {
        try {
            likeService.removeLike(userId, postId, trackId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Hoặc mã lỗi khác tùy vào trường hợp
        }
    }
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> getLikesCountByPostId(@PathVariable Long postId) {
        long count = likeService.countLikesByPostId(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/track/{trackId}/count")
    public ResponseEntity<Long> getLikesCountByTrackId(@PathVariable Long trackId) {
        long count = likeService.countLikesByTrackId(trackId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<LikeDto>> getLikesByPostId(@PathVariable Long postId) {
        List<LikeDto> likes = likeService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/track/{trackId}")
    public ResponseEntity<List<LikeDto>> getLikesByTrackId(@PathVariable Long trackId) {
        List<LikeDto> likes = likeService.getLikesByTrackId(trackId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<Boolean> checkUserLike(@PathVariable Long postId, @PathVariable Long userId) {
        boolean hasLiked = likeService.checkUserLike(postId, userId);
        return ResponseEntity.ok(hasLiked);
    }

    @GetMapping("/track/{trackId}/user/{userId}")
    public ResponseEntity<Boolean> checkUserLikeTrack(@PathVariable Long trackId, @PathVariable Long userId) {
        boolean hasLiked = likeService.checkUserLikeTrack(trackId, userId);
        return ResponseEntity.ok(hasLiked);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<LikeDto>> getAllLikedByUser(@PathVariable Long userId) {
        List<LikeDto> liked = likeService.getAllByUserId(userId);
        return ResponseEntity.ok(liked);
    }

//    album
    @GetMapping("/allAlbums/{userId}")
    public ResponseEntity<List<LikeDto>> getAllAlbumByUserId(@PathVariable Long userId) {
        List<LikeDto> liked = likeService.getAllAlbumByUserId(userId);

        // Lọc ra các LikeDto có albumId khác null
        List<LikeDto> filteredLikes = liked.stream()
                .filter(likeDto -> likeDto.getAlbumId() != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredLikes);
    }

    @GetMapping("/allPlaylist/{userId}")
    public ResponseEntity<List<LikeDto>> getAllPlayListByUserId(@PathVariable Long userId) {
        List<LikeDto> liked = likeService.getAllPlayListByUserId(userId);

        // Lọc ra các LikeDto có albumId khác null
        List<LikeDto> filteredLikes = liked.stream()
                .filter(likeDto -> likeDto.getPlaylistId() != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredLikes);
    }

}
