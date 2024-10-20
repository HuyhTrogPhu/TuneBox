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
}
