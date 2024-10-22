package org.example.library.service.implement;

import org.example.library.dto.ChatDto;
import org.example.library.model.Chat;
import org.example.library.model.User;
import org.example.library.repository.ChatRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ChatDto createChat(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setCreationDate(LocalDateTime.now());
        chat = chatRepository.save(chat);
        return convertToDTO(chat);
    }

    @Override
    public ChatDto getChatById(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        return convertToDTO(chat);
    }

    @Override
    public List<ChatDto> getChatsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Chat> chats = chatRepository.findBySenderOrReceiverOrderByCreationDateDesc(user, user);
        return chats.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ChatDto getChatBetweenUsers(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User 1 not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        Optional<Chat> chatOpt = chatRepository.findChatBetweenUsers(user1, user2);
        if (chatOpt.isPresent()) {
            return convertToDTO(chatOpt.get());
        } else {
            return createChat(user1Id, user2Id);
        }
    }

    @Override
    public void deleteChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }

    private ChatDto convertToDTO(Chat chat) {
        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());
        dto.setCreationDate(chat.getCreationDate());
        dto.setSenderId(chat.getSender().getId());
        dto.setReceiverId(chat.getReceiver().getId());
        return dto;
    }
}
