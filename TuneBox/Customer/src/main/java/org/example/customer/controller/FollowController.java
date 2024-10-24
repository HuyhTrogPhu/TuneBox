package org.example.customer.controller;

import org.example.library.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        try {
            followService.followUser(followerId, followedId);
            return ResponseEntity.ok("User followed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to follow user.");
        }
    }


    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        try {
            followService.unfollowUser(followerId, followedId);
            return ResponseEntity.ok("User unfollowed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to unfollow user.");
        }
    }


    @GetMapping("/is-following")
    public ResponseEntity<Boolean> isFollowing(@RequestParam Long followerId, @RequestParam Long followedId) {
        boolean isFollowing = followService.isFollowing(followerId, followedId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/followers-count")
    public ResponseEntity<Integer> getFollowersCount(@RequestParam Long userId) {
        int followersCount = followService.countFollowers(userId);
        return ResponseEntity.ok(followersCount);
    }

    @GetMapping("/following-count")
    public ResponseEntity<Integer> getFollowingCount(@RequestParam Long userId) {
        int followingCount = followService.countFollowing(userId);
        return ResponseEntity.ok(followingCount);
    }
}
