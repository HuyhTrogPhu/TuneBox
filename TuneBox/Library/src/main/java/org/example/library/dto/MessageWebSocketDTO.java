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
    private UserMessageDTO senderId;
    private UserMessageDTO receiverId;
    private String content;
    private LocalDateTime creationDate;
}
