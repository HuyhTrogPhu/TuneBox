package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private Long IdShare;
    private String content;
    private Long userId;
    private String userNickname;
    private Set<PostImageDto> images;
    private LocalDateTime createdAt;
    private boolean hidden;
    private boolean adminHidden;
    private String avatar;
    private long likeCount;
    private long commentCount;
}