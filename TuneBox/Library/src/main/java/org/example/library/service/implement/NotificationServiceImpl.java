package org.example.library.service.implement;

import org.example.library.dto.NotificationDTO;
import org.example.library.model.Follow;
import org.example.library.model.Notification;
import org.example.library.model.Post;
import org.example.library.model.User;
import org.example.library.repository.FollowRepository;
import org.example.library.repository.NotificationRepository;
import org.example.library.service.NotificationService;
import org.example.library.websocket.NotificationWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationWebSocket notificationWebSocket; // Đảm bảo đã khôi phục dòng này

    @Override
    public void notifyFollowers(User user, Post post) {
        // Lấy danh sách những người theo dõi người dùng
        List<Follow> followers = followRepository.findByFollowedId(user.getId());

        for (Follow follow : followers) {
            User followerUser = follow.getFollower(); // Lấy thông tin người theo dõi

            // Tạo thông báo
            NotificationDTO notification = new NotificationDTO(
                    null,
                    "User " + user.getUserName() + " has posted: " + post.getContent(),
                    LocalDateTime.now(),
                    followerUser.getId()
            );

            // Gửi thông báo qua WebSocket
            sendNotification(followerUser, notification);

            // Lưu thông báo vào cơ sở dữ liệu
            saveNotification(notification);
        }
    }

    private void sendNotification(User follower, NotificationDTO notification) {
        notificationWebSocket.sendNotification(follower.getId(), notification.getMessage());
    }

    private void saveNotification(NotificationDTO notification) {
        Notification newNotification = new Notification();
        newNotification.setMessage(notification.getMessage());
        newNotification.setUserId(notification.getUserId());
        newNotification.setCreatedAt(notification.getCreatedAt());
        notificationRepository.save(newNotification);
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map(notification -> new NotificationDTO(notification.getId(), notification.getMessage(), notification.getCreatedAt(), notification.getUserId()))
                .collect(Collectors.toList());
    }
}
