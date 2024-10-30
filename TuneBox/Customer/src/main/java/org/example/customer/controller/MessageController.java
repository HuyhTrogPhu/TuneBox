package org.example.customer.controller;

import org.example.library.dto.MessageDTO;
import org.example.library.dto.UserDto;
import org.example.library.mapper.ChatMessageMapper;
import org.example.library.model.Message;
import org.example.library.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatMessageMapper messageMapper;


    @GetMapping("/between")
    public ResponseEntity<List<MessageDTO>> getMessagesBetween(@RequestParam Long userId1, @RequestParam Long userId2) {
        try {
            // Lấy danh sách Message từ service
            List<Message> messages = messageService.getMessagesBetween(userId1, userId2);
            // Chuyển đổi danh sách Message thành danh sách MessageDTO
            List<MessageDTO> messageDTOs = messages.stream().map(message -> {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setId(message.getId());

                if (message.getSender() != null) {
                    messageDTO.setSenderId(message.getSender().getId());
                }

                if (message.getReceiver() != null) {
                    messageDTO.setReceiverId(message.getReceiver().getId());
                }


                messageDTO.setContent(message.getContent());
                messageDTO.setCreationDate(message.getDateTime());
                return messageDTO;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);
        } catch (Exception e) {
            logger.error("Error fetching messages between users {} and {}: {}", userId1, userId2, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}