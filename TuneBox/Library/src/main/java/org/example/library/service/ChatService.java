package org.example.library.service;

import org.example.library.dto.ChatDto;

import java.util.List;

public interface ChatService {
    ChatDto createChat(Long senderId, Long receiverId);
    ChatDto getChatById(Long chatId);
    List<ChatDto> getChatsByUser(Long userId);
    ChatDto  getChatBetweenUsers(Long user1Id, Long user2Id);
    void deleteChat(Long chatId);
}