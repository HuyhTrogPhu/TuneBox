package org.example.customer.controller;

import org.example.library.dto.FriendRequestDTO;
import org.example.library.model.Friend;
import org.example.library.model.User;
import org.example.library.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> checkFriendStatus(@RequestParam Long userId, @RequestParam Long friendId) {
        String status = friendService.checkFriendStatus(userId, friendId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/requests/{userId}")
    public List<FriendRequestDTO> getFriendRequests(@PathVariable Long userId) {
        return friendService.getPendingFriendRequests(userId);
    }
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long userId) {
        List<User> friends = friendService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }
    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getFriendCount(@PathVariable Long userId) {
        Long friendCount = friendService.countFriends(userId);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Friend count for userId " + userId + ": " + friendCount);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return ResponseEntity.ok(friendCount);
    }


}

