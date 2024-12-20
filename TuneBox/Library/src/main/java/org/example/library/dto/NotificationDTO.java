package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id; // ID thông báo
    private String message; // Nội dung thông báo
    private LocalDateTime createdAt; // Thời gian tạo thông báo
    private Long userId; // ID người nhận thông báo
    private String avatarUrl; // Đường dẫn đến avatar của người nhận thông báo
    private boolean isRead; // Trạng thái đã đọc thông báo
    private Long postId; // ID của bài viết mà thông báo này liên quan đến
    private String type;

    public NotificationDTO(String message, String type, LocalDateTime createdAt) {
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
    }

}