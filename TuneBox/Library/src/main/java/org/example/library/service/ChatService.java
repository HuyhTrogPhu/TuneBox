package org.example.library.service;

import org.example.library.model.Chat;
import org.example.library.model.User;

import java.util.List;

public interface ChatService {
    Chat createChat(User sender, User receiver);
    Chat getChatById(Long chatId);
    List<Chat> getChatsByUser(User user);
    Chat getChatBetweenUsers(User user1, User user2);
    void deleteChat(Long chatId);
}
