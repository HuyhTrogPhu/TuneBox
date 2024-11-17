package org.example.library.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserInteractionDto {
    private String userId;
    private String userName;
    private long postCount;
    private long totalLikes;
    private long totalComments;

    public UserInteractionDto() {
    }

    public UserInteractionDto(String userId, String userName, Long postCount, Long likeCount, Long commentCount) {
        this.userId = userId;
        this.userName = userName;
        this.postCount = postCount;
        this.totalLikes = likeCount;
        this.totalComments = commentCount;
    }
}