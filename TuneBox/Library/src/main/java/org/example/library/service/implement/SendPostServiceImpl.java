package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.model.Instrument;
import org.example.library.model.Message;
import org.example.library.model.Post;
import org.example.library.model.User;
import org.example.library.repository.InstrumentRepository;
import org.example.library.repository.MessageRepository;
import org.example.library.repository.PostRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.SendPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SendPostServiceImpl implements SendPostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MessageRepository messageRepository;
    private final MessageServiceImpl messageService;

    private static final Logger logger = LoggerFactory.getLogger(SendPostServiceImpl.class);

    @Override
    public void sendPostMessage(Long senderId, Long receiverId, Long postId) {
        // Tìm người gửi, người nhận và bản nhạc
        User sender = userRepository.findById(senderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Tạo URL cho intrument
        String postUrl = "http://localhost:3000/post/" + post.getId();

        // Tạo đối tượng Message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Shared Post: " + post.getContent() + " - " + postUrl);
        message.setDateTime(LocalDateTime.now());

        // Lưu tin nhắn vào cơ sở dữ liệu
        try {
            Message savedMessage = messageService.saveMessage(message);
            logger.info("Saved post message with ID: {} from sender ID: {} to receiver ID: {}",
                    savedMessage.getId(), senderId, receiverId);
        } catch (Exception e) {
            logger.error("Failed to save post message: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send post message");
        }
    }
}
