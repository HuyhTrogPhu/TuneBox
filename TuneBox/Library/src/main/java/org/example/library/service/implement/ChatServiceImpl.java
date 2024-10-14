package org.example.library.service.implement;

import org.example.library.model.Chat;
import org.example.library.model.User;
import org.example.library.repository.ChatRepository;
import org.example.library.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat createChat(User sender, User receiver) {
        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setCreationDate(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    @Override
    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    @Override
    public List<Chat> getChatsByUser(User user) {
        return chatRepository.findBySenderOrReceiverOrderByCreationDateDesc(user, user);
    }

    @Override
    public Chat getChatBetweenUsers(User user1, User user2) {
        return chatRepository.findChatBetweenUsers(user1, user2)
                .orElseGet(() -> createChat(user1, user2));
    }

    @Override
    public void deleteChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }
}