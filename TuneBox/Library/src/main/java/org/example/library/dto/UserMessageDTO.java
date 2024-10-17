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
    private UserDto senderId;
    private UserDto receiverId;
    private LocalDateTime creationDate;

    public UserMessageDTO(Long id) {
        this.id = id;
    }
}
