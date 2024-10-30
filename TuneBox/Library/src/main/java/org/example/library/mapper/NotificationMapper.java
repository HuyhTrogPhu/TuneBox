package org.example.library.mapper;

import org.example.library.dto.NotificationDTO;
import org.example.library.model.Notification;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    @Autowired
    private UserService userService; // Service để truy vấn User

    public NotificationDTO toDTO(Notification notification) {
        // Fetch user avatar từ UserService
        String avatarUrl = userService.getUserAvatar(notification.getUserId());

        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getCreatedAt(),
                notification.getUserId(),
                avatarUrl,
                notification.isRead(),
                notification.getPostId()
        );
    }

    public Notification toEntity(NotificationDTO notificationDTO) {
        return new Notification(
                notificationDTO.getId(),
                notificationDTO.getMessage(),
                notificationDTO.getUserId(),
                notificationDTO.getCreatedAt(),
                notificationDTO.isRead(),
                notificationDTO.getPostId()
        );
    }
}
