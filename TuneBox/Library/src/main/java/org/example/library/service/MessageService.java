package org.example.library.service;

import org.example.library.dto.ListUserForMessageDto;
import org.example.library.model.Message;

import java.util.List;

public interface MessageService {
    Message saveMessage(Message message);  // Lưu tin nhắn

    List<Message> getMessagesBetween(Long userId1, Long userId2); // Lấy tin nhắn giữa hai người dùng

    Message revokeMessage(Long messageId, Long userId); //thu hồi tin nhắn

    Message findMessageById(Long messageId); // Tìm tin nhắn theo ID

    List<ListUserForMessageDto> findAllAcceptedFriends(Long userId);

}
