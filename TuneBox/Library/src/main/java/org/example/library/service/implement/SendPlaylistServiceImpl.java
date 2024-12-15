package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.model.Albums;
import org.example.library.model.Message;
import org.example.library.model.Playlist;
import org.example.library.model.User;
import org.example.library.repository.MessageRepository;
import org.example.library.repository.PlaylistRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.SendPlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SendPlaylistServiceImpl implements SendPlaylistService {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final MessageRepository messageRepository;
    private final MessageServiceImpl messageService;

    private static final Logger logger = LoggerFactory.getLogger(SendPlaylistServiceImpl.class);

    @Override
    public void sendPlaylistMessage(Long senderId, Long receiverId, Long playlistId) {
        // Tìm người gửi, người nhận và bản nhạc
        User sender = userRepository.findById(senderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Albums not found"));

        // Tạo URL cho track
        String playlistUrl = "http://localhost:3000/playlist/" + playlist.getId();

        // Tạo đối tượng Message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Shared Playlist: " + playlist.getTitle() + " - " + playlistUrl);
        message.setDateTime(LocalDateTime.now());

        // Lưu tin nhắn vào cơ sở dữ liệu
        try {
            Message savedMessage = messageService.saveMessage(message);
            logger.info("Saved playlist message with ID: {} from sender ID: {} to receiver ID: {}",
                    savedMessage.getId(), senderId, receiverId);
        } catch (Exception e) {
            logger.error("Failed to save playlist message: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send playlist message");
        }
    }
}
