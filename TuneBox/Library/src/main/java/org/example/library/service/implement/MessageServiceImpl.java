package org.example.library.service.implement;

import org.example.library.model.Chat;
import org.example.library.model.Message;
import org.example.library.model.User;
import org.example.library.repository.ChatRepository;
import org.example.library.repository.MessageRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);


    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message); // Lưu tin nhắn
    }

    @Override
    public List<Message> getMessagesBetween(Long userId1, Long userId2) {
        logger.info("Fetching messages between users {} and {}", userId1, userId2);
        List<Message> messages = messageRepository.findMessagesBetween(userId1, userId2);
        logger.info("Found {} messages", messages.size());
        return messages;
    }
}
