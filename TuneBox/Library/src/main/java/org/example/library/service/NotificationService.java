package org.example.library.service;

import org.example.library.dto.NotificationDTO;
import org.example.library.model.Post;
import org.example.library.model.User;

import java.util.List;

public interface NotificationService {
    void sendNotificationToFollowers(NotificationDTO notificationDTO, Long targetUserId);

    void notifyFollowersOfNewPost(Long postId, Long userId); // Thay đổi ở đây

    List<NotificationDTO> getUserNotifications(Long userId);

    void markNotificationAsRead(Long notificationId);
//
//    void sendWarningToUser(Long userId, String title, String message);
//    void notifyReporter(Long userId, String title, String message);
//    // Phương thức gửi thông báo khi có người thích bài viết
    void sendLikeNotification(User liker, Post post);

    void sendNotificationcomment(Long userId, String message, Long postId);

    void deleteNotification(Long notificationId);

    void deleteAllReadNotifications(Long userId);
}

