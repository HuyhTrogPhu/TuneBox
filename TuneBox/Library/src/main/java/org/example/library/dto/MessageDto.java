package org.example.library.dto;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;
import org.example.library.model.Chat;
import org.example.library.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long senderId;
    private Long receiverId;
    private Long chatId;
    private List<OtherAttachmentDto> attachments;

}
