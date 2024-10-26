package org.example.library.service;

import org.example.library.dto.NotificationDTO;
import org.example.library.model.Post;

import java.util.List;

public interface NotificationService {
    void sendNotificationToFollowers(NotificationDTO notificationDTO, Long targetUserId);

    void notifyFollowersOfNewPost(Long postId, Long userId); // Thay đổi ở đây

    List<NotificationDTO> getUserNotifications(Long userId);

    void markNotificationAsRead(Long notificationId);

    void sendWarningToUser(Long userId, String title, String message);
    void notifyReporter(Long userId, String title, String message);
}