package org.example.library.service.implement;

import org.example.library.Exception.MessageRevocationException;
import org.example.library.Exception.MessageRevocationTimeoutException;
import org.example.library.Exception.ResourceNotFoundException;
import org.example.library.Exception.UnauthorizedException;
import org.example.library.dto.ListUserForMessageDto;
import org.example.library.model.Chat;
import org.example.library.model.Message;
import org.example.library.model.OtherAttachment;
import org.example.library.model.User;
import org.example.library.model_enum.MessageStatus;
import org.example.library.repository.ChatRepository;
import org.example.library.repository.MessageRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private CloudinaryService cloudinaryService;

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
                attachment.setMessage(message);
                // Set default content type if not provided
                if (attachment.getContentType() == null) {
                    attachment.setContentType(attachment.getMimeType());
                }
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

    @Override
     public Message findMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    @Override
    public List<ListUserForMessageDto> findAllAcceptedFriends(Long userId) {
        List<User> friends = messageRepository.findAcceptedFriends(userId);

        return friends.stream()
                .map(friend -> {
                    ListUserForMessageDto dto = new ListUserForMessageDto();
                    dto.setId(friend.getId());
                    dto.setUsername(friend.getUserName());
                    // Assuming UserInformation exists
                    if (friend.getUserInformation() != null) {
                        dto.setNickName(friend.getUserInformation().getName());
                        dto.setAvatar(friend.getUserInformation().getAvatar());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Message revokeMessage(Long messageId, Long userId) {
        // 1. Validate input
        if (messageId == null || userId == null) {
            throw new IllegalArgumentException("MessageId and userId cannot be null");
        }

        // 2. Find and validate message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));

        // 3. Validate ownership
        if (!message.getSender().getId().equals(userId)) {
            throw new UnauthorizedException("User " + userId + " is not authorized to revoke message " + messageId);
        }

        // 4. Validate time window (5 minutes)
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(message.getDateTime().plusMinutes(5))) {
            throw new MessageRevocationTimeoutException("Cannot revoke message after 5 minutes");
        }

        try {
            // 5. Handle attachments if present
            if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
                for (OtherAttachment attachment : message.getAttachments()) {
                    try {
                        // Delete from cloud storage
                        String publicId = extractPublicIdFromUrl(attachment.getFileUrl());
                        if (publicId != null) {
                            cloudinaryService.deleteFile(publicId);
                        }

                        // Clear attachment data
                        attachment.setFileUrl(null);
                        attachment.setFileName("File đã bị thu hồi");
                        attachment.setMimeType(null);
                        attachment.setSize(0L);
                    } catch (Exception e) {
                        logger.error("Failed to delete attachment for message {}: {}", messageId, e.getMessage());
                        // Continue with other attachments even if one fails
                    }
                }
            }

            // 6. Update message status
            message.setStatus(MessageStatus.REVOKED);
            message.setContent("Tin nhắn đã được thu hồi");
            message.setRevokedAt(now);

            // 7. Save and return updated message
            Message revokedMessage = messageRepository.save(message);
            logger.info("Successfully revoked message with id {}", messageId);

            return revokedMessage;

        } catch (Exception e) {
            logger.error("Error while revoking message {}: {}", messageId, e.getMessage());
            throw new MessageRevocationException("Failed to revoke message: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String url) {
        if (url == null) return null;

        try {
            // Example URL: https://res.cloudinary.com/your-cloud-name/image/upload/v1234567/public_id.jpg
            String[] parts = url.split("/");
            String fileName = parts[parts.length - 1];
            // Remove file extension
            return fileName.substring(0, fileName.lastIndexOf('.'));
        } catch (Exception e) {
            logger.warn("Could not extract public ID from URL: {}", url);
            return null;
        }
    }
}
