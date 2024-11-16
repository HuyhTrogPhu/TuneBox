package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.model.Albums;
import org.example.library.model.Message;
import org.example.library.model.User;
import org.example.library.repository.AlbumsRepository;
import org.example.library.repository.MessageRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.SendAlbumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SendAlbumServiceImpl implements SendAlbumService {
    private final UserRepository userRepository;
    private final AlbumsRepository albumsRepository;
    private final MessageRepository messageRepository;
    private final MessageServiceImpl messageService;

    private static final Logger logger = LoggerFactory.getLogger(SendAlbumServiceImpl.class);

    @Override
    public void sendAlbumMessage(Long senderId, Long receiverId, Long albumId) {
        // Tìm người gửi, người nhận và bản nhạc
        User sender = userRepository.findById(senderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));
        Albums albums = albumsRepository.findById(albumId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Albums not found"));

        // Tạo URL cho track
        String albumsUrl = "http://localhost:3000/album/" + albums.getId();

        // Tạo đối tượng Message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Shared Album: " + albums.getTitle() + " - " + albumsUrl);
        message.setDateTime(LocalDateTime.now());

        // Lưu tin nhắn vào cơ sở dữ liệu
        try {
            Message savedMessage = messageService.saveMessage(message);
            logger.info("Saved album message with ID: {} from sender ID: {} to receiver ID: {}",
                    savedMessage.getId(), senderId, receiverId);
        } catch (Exception e) {
            logger.error("Failed to save album message: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send album message");
        }
    }
}
