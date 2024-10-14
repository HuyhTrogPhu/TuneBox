package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long userId;
    private String userNickname;
    private Long commentId;
    private Long parentReplyId;
}
