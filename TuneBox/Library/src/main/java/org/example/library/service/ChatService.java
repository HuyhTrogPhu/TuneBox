package org.example.library.service;

import org.example.library.dto.ChatDTO;

import java.util.List;

public interface ChatService {
    ChatDTO createChat(Long senderId, Long receiverId);
    ChatDTO getChatById(Long chatId);
    List<ChatDTO> getChatsByUser(Long userId);
    ChatDTO  getChatBetweenUsers(Long user1Id, Long user2Id);
    void deleteChat(Long chatId);
}