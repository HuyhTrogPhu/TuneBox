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
public class MessageWebSocketDTO {
    private Long id;
    private UserDto senderId;  // Sử dụng Long ở đây
    private UserDto receiverId; // Sử dụng Long ở đây
    private String content;
    private LocalDateTime creationDate;
}
