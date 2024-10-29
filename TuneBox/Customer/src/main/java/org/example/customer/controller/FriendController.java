package org.example.customer.controller;

import org.example.library.dto.FriendAcceptDto;
import org.example.library.dto.FriendRequestDTO;
import org.example.library.model.Friend;
import org.example.library.model.User;
import org.example.library.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    // send request to user other
    @PostMapping("/{userId}/{friendId}")
    public ResponseEntity<Long> sendFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        Long requestId = friendService.sendFriendRequest(userId, friendId);
        return ResponseEntity.ok(requestId); // Trả về requestId
    }

    // accept request to user other
    @PostMapping("/accept/{requestId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // decline request to user other
    @PostMapping("/decline/{requestId}")
    public ResponseEntity<Void> declineFriendRequest(@PathVariable Long requestId) {
        friendService.declineFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // unfriend with user other
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> unfriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.unfriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    // cancel request to user other
    @DeleteMapping("/cancel-request/{userId}/{friendId}")
    public ResponseEntity<Void> cancelFriendRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.cancelFriendRequest(userId, friendId);
        return ResponseEntity.ok().build();
    }

    // check friend status between two users
    @GetMapping("/check")
    public ResponseEntity<String> checkFriendStatus(@RequestParam Long userId, @RequestParam Long friendId) {
        String status = friendService.checkFriendStatus(userId, friendId);
        return ResponseEntity.ok(status);
    }

    // get friend pending from user other
    @GetMapping("/requests/{userId}")
    public List<FriendRequestDTO> getFriendRequests(@PathVariable Long userId) {
        return friendService.getPendingFriendRequests(userId);
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

    // get friend count of user other
    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getFriendCount(@PathVariable Long userId) {
        Long friendCount = friendService.countFriends(userId);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Friend count for userId " + userId + ": " + friendCount);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return ResponseEntity.ok(friendCount);
    }


}

