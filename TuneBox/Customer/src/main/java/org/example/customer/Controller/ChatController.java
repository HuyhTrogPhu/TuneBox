package org.example.customer.controller;

import org.example.library.dto.ChatDto;
import org.example.library.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable Long chatId) {
        ChatDto chat = chatService.getChatById(chatId);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDto>> getChatsByUser(@PathVariable Long userId) {
        List<ChatDto> chats = chatService.getChatsByUser(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/between")
    public ResponseEntity<ChatDto> getChatBetweenUsers(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        ChatDto chat = chatService.getChatBetweenUsers(user1Id, user2Id);
        return ResponseEntity.ok(chat);
    }

    @PostMapping
    public ResponseEntity<ChatDto> createChat(@RequestParam Long senderId, @RequestParam Long receiverId) {
        ChatDto chat = chatService.createChat(senderId, receiverId);
        return ResponseEntity.ok(chat);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }
}