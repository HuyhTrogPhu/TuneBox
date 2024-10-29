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
public class MessageDTO {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long senderId;
    private Long receiverId;
    private Long chatId;
    private Long trackId;
    private String trackName;
    private List<OtherAttachmentDto> attachments;

}
