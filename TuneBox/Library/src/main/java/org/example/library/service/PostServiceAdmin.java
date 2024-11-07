package org.example.library.service;

import org.example.library.dto.*;

import java.util.List;

public interface PostServiceAdmin {
    List<PostAdminDto> getTopPostsByInteractions();

    List<PostAdminDto> findPostsWithImages();

    List<PostAdminDto> findPostsWithoutImages();

    List<PostAdminDto> findAllByOrderByCreatedAtDesc();

    PostStatisticsDto getPostStatistics();

    List<PostEngagementDto> getPostEngagementStats();

    List<DailyPostStatsDto> getDailyPostStats();
}
