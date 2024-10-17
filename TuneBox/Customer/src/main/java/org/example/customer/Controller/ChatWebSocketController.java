package org.example.customer.controller;

import org.example.library.dto.MessageDTO;
import org.example.library.dto.MessageWebSocketDTO;
import org.example.library.dto.UserDto;
import org.example.library.dto.UserMessageDTO;
import org.example.library.mapper.ChatMessageMapper;
import org.example.library.model.Message;
import org.example.library.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

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
            Message message = messageMapper.toModel(messageWebSocketDTO); // Giả sử bạn đã điều chỉnh để hỗ trợ MessageWebSocketDTO

            Message savedMessage = messageService.saveMessage(message);
            MessageWebSocketDTO savedMessageDTO = new MessageWebSocketDTO();
            savedMessageDTO.setId(savedMessage.getId());
            savedMessageDTO.setSenderId(new UserMessageDTO(savedMessage.getSender().getId())); // Giả sử sender là một đối tượng UserDto
            savedMessageDTO.setReceiverId(new UserMessageDTO(savedMessage.getReceiver().getId())); // Giả sử receiver là một đối tượng UserDto
            savedMessageDTO.setContent(savedMessage.getContent());
            savedMessageDTO.setCreationDate(savedMessage.getDateTime());

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