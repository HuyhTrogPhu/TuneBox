package org.example.library.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostStatisticsDto {
    private long totalPosts;
    private long postsWithImages;
    private long postsWithoutImages;
    private long hiddenPosts;
    private double averageLikesPerPost;
    private double averageCommentsPerPost;
    private long totalLikes;
    private long totalComments;
    private long postsLastWeek;
    private long postsLastMonth;
}
