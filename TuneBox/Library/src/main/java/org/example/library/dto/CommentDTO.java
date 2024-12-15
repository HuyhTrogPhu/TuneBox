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
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long userId;
    private String userNickname;
    private Long postId;
    private Long parentId; // Thêm trường parentId
    private List<CommentDTO> replies;
    private boolean edited;
    private Long trackId;
    private String icon;
    private String avatar;
}
