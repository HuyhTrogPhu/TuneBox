package org.example.customer.controller;

import org.example.library.dto.MessageDTO;
import org.example.library.dto.UserDto;
import org.example.library.mapper.MessageMapper;
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



    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDTO messageDTO) {
        Message message = MessageMapper.INSTANCE.toModel(messageDTO);
        Message savedMessage = messageService.saveMessage(message);
        MessageDTO savedMessageDTO = MessageMapper.INSTANCE.toDTO(savedMessage);

        // Send to sender
        messagingTemplate.convertAndSendToUser(
                String.valueOf(savedMessage.getSender().getId()),
                "/queue/messages",
                savedMessageDTO
        );

        // Send to receiver
        messagingTemplate.convertAndSendToUser(
                String.valueOf(savedMessage.getReceiver().getId()),
                "/queue/messages",
                savedMessageDTO
        );
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());

        UserDto senderDto = new UserDto();
        senderDto.setId(message.getSender().getId());
        dto.setSenderId(senderDto);

        UserDto receiverDto = new UserDto();
        receiverDto.setId(message.getReceiver().getId());
        dto.setReceiverId(receiverDto);

        dto.setCreationDate(message.getDateTime());
        return dto;
    }



    @MessageMapping("/chat.addUser")
    @SendTo("/topic/chat/{chatId}")
    public MessageDTO addUser(@Payload MessageDTO messageDTO, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable Long chatId) {
        headerAccessor.getSessionAttributes().put("username", messageDTO.getSenderId().getUserName());
        headerAccessor.getSessionAttributes().put("userId", messageDTO.getSenderId().getId());
        return messageDTO;
    }
}