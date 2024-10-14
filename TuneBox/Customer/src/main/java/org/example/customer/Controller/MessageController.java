package org.example.customer.controller;

import org.example.library.dto.MessageDTO;
import org.example.library.dto.UserDto;
import org.example.library.model.Chat;
import org.example.library.model.Message;
import org.example.library.model.User;
import org.example.library.repository.ChatRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class); // Khai báo logger

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageRequest messageRequest) {
        // Kiểm tra xem senderId và receiverId có null không
        if (messageRequest.getSenderId() == null || messageRequest.getReceiverId() == null) {
            throw new IllegalArgumentException("Sender ID and Receiver ID cannot be null");
        }
        User sender = userRepository.findById(messageRequest.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(messageRequest.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
        // Kiểm tra nếu chat đã tồn tại giữa hai người dùng
        Chat chat = chatRepository.findBySenderAndReceiver(sender, receiver)
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat.setSender(sender);
                    newChat.setReceiver(receiver);
                    return chatRepository.save(newChat);
                });
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setChat(chat);
        message.setMessage(messageRequest.getContent());
        message.setDateTime(LocalDateTime.now());
        // Lưu message
        Message savedMessage = messageService.saveMessage(message);
        // Chuyển đổi sang DTO
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(savedMessage.getId());
        messageDTO.setMessage(savedMessage.getMessage());
        messageDTO.setDateTime(savedMessage.getDateTime());
        UserDto userDto = new UserDto();
        userDto.setId(savedMessage.getSender().getId());
        userDto.setEmail(savedMessage.getSender().getEmail());
        userDto.setUserName(savedMessage.getSender().getUserName());
        userDto.setUserNickname(savedMessage.getSender().getUserNickname());

        messageDTO.setSender(userDto);
        messageDTO.setChatId(savedMessage.getChat().getId());

        return ResponseEntity.ok(messageDTO);
    }


    @GetMapping("/between")
    public ResponseEntity<List<MessageDTO>> getMessagesBetween(@RequestParam Long userId1, @RequestParam Long userId2) {
        try {
            List<Message> messages = messageService.getMessagesBetween(userId1, userId2);
            List<MessageDTO> messageDTOs = messages.stream().map(message -> {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getMessage());
                messageDTO.setDateTime(message.getDateTime());
                UserDto userDto = new UserDto();
                userDto.setId(message.getSender().getId());
                userDto.setEmail(message.getSender().getEmail());
                userDto.setUserName(message.getSender().getUserName());
                userDto.setUserNickname(message.getSender().getUserNickname());
                messageDTO.setSender(userDto);
                messageDTO.setChatId(message.getChat().getId());
                return messageDTO;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);
        } catch (Exception e) {
            logger.error("Error fetching messages between users {} and {}: {}", userId1, userId2, e.getMessage());
            // Create an empty list instead of a String message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    private static class MessageRequest {
        private Long senderId;
        private Long receiverId;
        private String content;

        // Getters and setters
        public Long getSenderId() {
            return senderId;
        }

        public void setSenderId(Long senderId) {
            this.senderId = senderId;
        }

        public Long getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(Long receiverId) {
            this.receiverId = receiverId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
