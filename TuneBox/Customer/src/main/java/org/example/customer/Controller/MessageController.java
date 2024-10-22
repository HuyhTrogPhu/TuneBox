package org.example.customer.controller;

import org.example.library.dto.MessageDTO;
import org.example.library.dto.OtherAttachmentDto;
import org.example.library.dto.UserDto;
import org.example.library.mapper.ChatMessageMapper;
import org.example.library.model.Message;
import org.example.library.service.FileStorageService;
import org.example.library.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    private FileStorageService fileStorageService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatMessageMapper messageMapper;

    @Value("${file.upload-dir}") // Định nghĩa trong application.properties
    private String uploadDir;

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
            // Lưu file và lấy tên file đã được tạo unique
            String fileName = fileStorageService.storeFile(file);

            // Tạo URL để truy cập file
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(fileName)
                    .toUriString();

            // Tạo response
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileUrl", fileUrl);
            response.put("size", String.valueOf(file.getSize()));
            response.put("type", file.getContentType());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi upload file: " + e.getMessage());
        }
    }
}