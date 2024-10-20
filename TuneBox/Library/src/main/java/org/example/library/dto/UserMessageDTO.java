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
public class UserMessageDTO {

    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime creationDate;

    public UserMessageDTO(Long id) {
        this.id = id;
    }

    public UserMessageDTO(Long id,Long senderId, Long receiverId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
