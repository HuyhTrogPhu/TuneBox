package org.example.library.service;

import org.example.library.dto.NotificationDTO;
import org.example.library.model.Post;
import org.example.library.model.User;

import java.util.List;

public interface NotificationService {
    void notifyFollowers(User user, Post post);
    List<NotificationDTO> getNotificationsByUserId(Long userId);
}
