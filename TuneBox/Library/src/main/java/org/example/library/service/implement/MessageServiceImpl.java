package org.example.library.service.implement;

import org.example.library.model.Chat;
import org.example.library.model.Message;
import org.example.library.model.OtherAttachment;
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

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public Message saveMessage(Message message) {
        if (message.getSender() == null || message.getSender().getId() == null) {
            throw new IllegalArgumentException("Sender cannot be null");
        }
        if (message.getReceiver() == null || message.getReceiver().getId() == null) {
            throw new IllegalArgumentException("Receiver cannot be null");
        }
        User sender = userRepository.findById(message.getSender().getId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(message.getReceiver().getId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setDateTime(LocalDateTime.now());

        if (message.getAttachments() != null) {
            for (OtherAttachment attachment : message.getAttachments()) {
                attachment.setMessage(message); // Thiết lập quan hệ với tin nhắn
            }
        }
        Message savedMessage = messageRepository.save(message);
        logger.info("Saved message: {}", savedMessage);
        return savedMessage;
    }

    @Override
    public List<Message> getMessagesBetween(Long userId1, Long userId2) {
        logger.info("Fetching messages between users {} and {}", userId1, userId2);
        List<Message> messages = messageRepository.findMessagesBetween(userId1, userId2);
        logger.info("Found {} messages", messages.size());
        return messages;
    }
}
