package org.example.library.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public PostEngagementDto() {
    }

    // Constructor đầy đủ cho JPQL
    public PostEngagementDto(
            Long postId,
            String userName,
            int likeCount,
            int commentCount,
            int shareCount,
            double engagementRate,
            LocalDateTime createdAt
    ) {
        this.postId = postId;
        this.userName = userName;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.engagementRate = engagementRate;
        this.createdAt = createdAt;
    }

    public PostEngagementDto(
            Long postId,
            String userName,
            long likeCount,
            long commentCount,
            int shareCount,
            LocalDateTime createdAt
    ) {
        this.postId = postId;
        this.userName = userName;
        this.likeCount = (int) likeCount;  // Chuyển đổi từ long sang int
        this.commentCount = (int) commentCount;  // Chuyển đổi từ long sang int
        this.shareCount = shareCount;
        this.createdAt = createdAt;
        this.engagementRate = 0.0;  // Có thể thiết lập mặc định hoặc tính toán sau
    }


}
