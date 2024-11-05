package org.example.customer.controller;

import org.example.library.dto.FollowCountsDto;
import org.example.library.dto.FollowedUserDto;
import org.example.library.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // get follower and following count by userId
    @GetMapping("/{userId}/followCounts")
    public ResponseEntity<FollowCountsDto> getFollowCounts(@PathVariable Long userId) {
        try {
            int followersCount = followService.countFollowers(userId);
            int followingCount = followService.countFollowing(userId);

            FollowCountsDto followCountsDto = new FollowCountsDto(followersCount, followingCount);
            return ResponseEntity.ok(followCountsDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowedUserDto>> getFollowers(@PathVariable Long userId) {
        try {
            List<FollowedUserDto> followers = followService.getFollowers(userId); // Đảm bảo gọi đúng phương thức
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowedUserDto>> getFollowing(@PathVariable Long userId) {
        try {
            List<FollowedUserDto> following = followService.getFollowing(userId); // Đảm bảo gọi đúng phương thức
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}