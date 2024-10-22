package org.example.customer.controller;

import org.example.library.model.Friend;
import org.example.library.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService; // Sử dụng interface

    @PostMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.sendFriendRequest(userId, friendId);
        return ResponseEntity.ok().build();
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
    @DeleteMapping("/{userId}/{friendId}") // Sử dụng DELETE cho hủy kết bạn
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
    public ResponseEntity<String> checkFriendStatus(@RequestParam Long userId, @RequestParam Long friendId) {
        String status = friendService.checkFriendStatus(userId, friendId);
        return ResponseEntity.ok(status);
    }
    @GetMapping("/requests/{userId}")
    public ResponseEntity<List<Friend>> getPendingRequests(@PathVariable Long userId) {
        List<Friend> requests = friendService.getPendingFriendRequests(userId);
        return ResponseEntity.ok(requests);
    }


}
