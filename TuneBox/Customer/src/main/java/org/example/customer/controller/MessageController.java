package org.example.customer.controller;

import org.example.library.dto.*;
import org.example.library.mapper.ChatMessageMapper;
import org.example.library.model.Message;
import org.example.library.repository.MessageRepository;
import org.example.library.service.MessageService;
import org.example.library.service.implement.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/messages")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/friends")
    public ResponseEntity<List<ListUserForMessageDto>> getAllFriendsForChat(@RequestParam Long userId) {
        try {
            List<ListUserForMessageDto> friends = messageService.findAllAcceptedFriends(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            logger.error("Error fetching friends for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/between")
    public ResponseEntity<List<MessageDTO>> getMessagesBetween(@RequestParam Long userId1, @RequestParam Long userId2) {
        try {
            List<Message> messages = messageService.getMessagesBetween(userId1, userId2);
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

                // Xử lý attachments
                if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
                    List<OtherAttachmentDto> attachmentDTOs = message.getAttachments().stream()
                            .map(attachment -> {
                                OtherAttachmentDto attachmentDTO = new OtherAttachmentDto();
                                attachmentDTO.setFileName(attachment.getFileName());
                                attachmentDTO.setFileUrl(attachment.getFileUrl());
                                attachmentDTO.setMimeType(attachment.getMimeType());
                                attachmentDTO.setSize(attachment.getSize());
                                return attachmentDTO;
                            })
                            .collect(Collectors.toList());
                    messageDTO.setAttachments(attachmentDTOs);
                }

                return messageDTO;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);
        } catch (Exception e) {
            logger.error("Error fetching messages between users {} and {}: {}", userId1, userId2, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Upload to Cloudinary
            Map uploadResult = cloudinaryService.uploadFile(file);

            // Get the secure URL from Cloudinary
            String fileUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            // Create response
            Map<String, String> response = new HashMap<>();
            response.put("fileName", file.getOriginalFilename());
            response.put("fileUrl", fileUrl);
            response.put("publicId", publicId);
            response.put("size", String.valueOf(file.getSize()));
            response.put("type", file.getContentType());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> revokeMessage(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Message revokedMessage = messageService.revokeMessage(id, userId);

            MessageWebSocketDTO revokedMessageDTO = new MessageWebSocketDTO();
            revokedMessageDTO.setId(revokedMessage.getId());
            revokedMessageDTO.setSenderId(new UserMessageDTO(revokedMessage.getSender().getId()));
            revokedMessageDTO.setReceiverId(new UserMessageDTO(revokedMessage.getReceiver().getId()));
            revokedMessageDTO.setContent(revokedMessage.getContent());
            revokedMessageDTO.setCreationDate(revokedMessage.getDateTime());
            revokedMessageDTO.setStatus(revokedMessage.getStatus().name());

            // Gửi thông báo qua WebSocket để cập nhật realtime
            template.convertAndSendToUser(
                    revokedMessage.getReceiver().getId().toString(),
                    "/queue/messages",
                    revokedMessageDTO
            );

            return ResponseEntity.ok(revokedMessageDTO);
        } catch (RuntimeException e) {
            logger.error("Error revoking message with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error revoking message: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error revoking message with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error revoking message: " + e.getMessage());
        }
    }
}