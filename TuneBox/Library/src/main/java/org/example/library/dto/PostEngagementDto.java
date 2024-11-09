package org.example.library.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostEngagementDto {
    private Long postId;
    private String userName;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private double engagementRate;
    private LocalDateTime createdAt;
}
