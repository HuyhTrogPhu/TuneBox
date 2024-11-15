package org.example.library.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostStatisticsResponseDto {
    private Long postId;
    private String content;
    private String userName;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private int totalInteractions;
}