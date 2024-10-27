package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private Long id;
    private LocalDateTime creationDate;
    private Long senderId;
    private Long receiverId;
    private List<MessageDTO> messages;
}
