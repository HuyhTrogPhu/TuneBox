package org.example.customer.controller;

import org.example.library.dto.FriendAcceptDto;
import org.example.library.dto.FriendRequestDto;
import org.example.library.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @PostMapping("/{userId}/{friendId}")
    public ResponseEntity<Long> sendFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        Long requestId = friendService.sendFriendRequest(userId, friendId);
        return ResponseEntity.ok(requestId); // Trả về requestId
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decline/{requestId}")
    public ResponseEntity<Void> declineFriendRequest(@PathVariable Long requestId) {
        friendService.declineFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> unfriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.unfriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel-request/{userId}/{friendId}")
    public ResponseEntity<Void> cancelFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.cancelFriendRequest(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFriendStatus(@RequestParam Long userId, @RequestParam Long friendId) {
        Map<String, Object> status = friendService.checkFriendStatus(userId, friendId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/requests/{userId}")
    public List<FriendRequestDto> getFriendRequests(@PathVariable Long userId) {
        return friendService.getFriendRequests(userId);
    }

    // get friends of user other
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getFriends(@PathVariable Long userId) {
        try {
            List<FriendAcceptDto> friends = friendService.getFriends(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getFriendCount(@PathVariable Long userId) {
        Long friendCount = friendService.countFriends(userId);
        return ResponseEntity.ok(friendCount);
    }


}

