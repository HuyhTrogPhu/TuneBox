package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.model.Message;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.MessageRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.SendTrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SendTrackServiceImpl implements SendTrackService {
    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final MessageRepository messageRepository;
    private final MessageServiceImpl messageService;
    private static final Logger logger = LoggerFactory.getLogger(SendTrackServiceImpl.class);

    @Override
    public void sendTrackMessage(Long senderId, Long receiverId, Long trackId) {
        // Tìm người gửi, người nhận và bản nhạc
        User sender = userRepository.findById(senderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));
        Track track = trackRepository.findById(trackId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found"));

        // Tạo URL cho track
        String trackUrl = "http://localhost:3000/track/" + track.getId();

        // Tạo đối tượng Message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Shared track: " + track.getName() + " - " + trackUrl);
        message.setDateTime(LocalDateTime.now());

        // Lưu tin nhắn vào cơ sở dữ liệu
        Message savedMessage = messageService.saveMessage(message);
        logger.info("Saved message with track URL: {}", savedMessage);
    }
}