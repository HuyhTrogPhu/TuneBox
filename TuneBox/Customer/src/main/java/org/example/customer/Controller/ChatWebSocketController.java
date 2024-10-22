package org.example.customer.controller;

import org.example.library.dto.*;
import org.example.library.mapper.ChatMessageMapper;
import org.example.library.model.Message;
import org.example.library.model.OtherAttachment;
import org.example.library.service.FileStorageService;
import org.example.library.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ChatWebSocketController {



    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageMapper messageMapper;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageWebSocketDTO messageWebSocketDTO) {
        try {
            // Chuyển đổi MessageWebSocketDTO thành Message
            Message message = messageMapper.toModel(messageWebSocketDTO);

            Message savedMessage = messageService.saveMessage(message);

            MessageWebSocketDTO savedMessageDTO = messageMapper.toDto(savedMessage);
            savedMessageDTO.setId(savedMessage.getId());
            savedMessageDTO.setSenderId
                    (new UserMessageDTO(savedMessage.getSender().getId()));
            savedMessageDTO.setReceiverId
                    (new UserMessageDTO(savedMessage.getReceiver().getId()));
            savedMessageDTO.setContent(savedMessage.getContent());
            savedMessageDTO.setCreationDate(savedMessage.getDateTime());

            if (messageWebSocketDTO.getAttachments() != null && !messageWebSocketDTO.getAttachments().isEmpty()) {
                List<OtherAttachment> attachments = messageWebSocketDTO.getAttachments().stream()
                        .map(attachmentDto -> {
                            OtherAttachment attachment = new OtherAttachment();
                            attachment.setFileName(attachmentDto.getFileName());
                            attachment.setFileUrl(attachmentDto.getFileUrl());
                            attachment.setMimeType(attachmentDto.getMimeType());
                            attachment.setSize(attachmentDto.getSize());
                            attachment.setMessage(message);
                            return attachment;
                        })
                        .collect(Collectors.toList());
                message.setAttachments(attachments);
            }


            // Gửi tin nhắn cho người gửi
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(savedMessageDTO.getSenderId().getId()),
                    "/queue/messages",
                    savedMessageDTO
            );

            // Gửi tin nhắn cho người nhận
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(savedMessageDTO.getReceiverId().getId()),
                    "/queue/messages",
                    savedMessageDTO
            );
        } catch (Exception e) {
            e.printStackTrace();
            // Bạn có thể muốn gửi thông báo lỗi lại cho client
        }
    }

//    private MessageDTO convertToDTO(Message message) {
//        MessageDTO dto = new MessageDTO();
//        dto.setId(message.getId());
//        dto.setContent(message.getContent());
//        dto.setSenderId(message.getSender().getId());
//        dto.setReceiverId(message.getReceiver().getId());
//        dto.setCreationDate(message.getDateTime());
//        return dto;
//    }



    @MessageMapping("/chat.addUser")
    @SendTo("/topic/chat/{chatId}")
    public MessageDTO addUser(@Payload MessageDTO messageDTO, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable Long chatId) {
        headerAccessor.getSessionAttributes().put("userId", messageDTO.getSenderId());
        return messageDTO;
    }
}