package org.example.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostReactionDto {
    private Long id;
    private long likeCount;
    private long commentCount;

    public PostReactionDto(Long id, int likeCount, int commentCount) {
        this.id = id;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
