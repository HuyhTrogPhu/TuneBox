package org.example.library.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageWebSocketDTO {
    private Long id;
    private UserMessageDTO senderId;
    private UserMessageDTO receiverId;
    private String content;
    private LocalDateTime creationDate;
    private List<OtherAttachmentDto> attachments;
}
