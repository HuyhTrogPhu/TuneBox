package org.example.customer.controller;

import org.example.library.dto.UserDto;
import org.example.library.model.Chat;
import org.example.library.model.User;
import org.example.library.service.ChatService;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    private User convertToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        // Set other fields as necessary
        return user;
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestParam Long senderId, @RequestParam Long receiverId) {
        User sender = convertToUser(userService.getUserById(senderId));
        User receiver = convertToUser(userService.getUserById(receiverId));
        Chat chat = chatService.createChat(sender, receiver);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long chatId) {
        Chat chat = chatService.getChatById(chatId);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getChatsByUser(@PathVariable Long userId) {
        User user = convertToUser(userService.getUserById(userId));
        List<Chat> chats = chatService.getChatsByUser(user);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/between")
    public ResponseEntity<Chat> getChatBetweenUsers(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        User user1 = convertToUser(userService.getUserById(user1Id));
        User user2 = convertToUser(userService.getUserById(user2Id));
        Chat chat = chatService.getChatBetweenUsers(user1, user2);
        return ResponseEntity.ok(chat);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }
}