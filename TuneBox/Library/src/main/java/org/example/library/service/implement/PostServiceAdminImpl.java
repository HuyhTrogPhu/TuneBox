package org.example.library.service.implement;

import org.example.library.dto.*;
import org.example.library.model.Post;
import org.example.library.repository.PostAdminRepository;
import org.example.library.service.PostServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceAdminImpl implements PostServiceAdmin {

    @Autowired
    private PostAdminRepository postAdminRepository;

    @Override
    public List<PostAdminDto> getTopPostsByInteractions() {
        List<Post> posts = postAdminRepository.findTopPostsByInteractions();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findPostsWithImages() {
        List<Post> posts = postAdminRepository.findPostsWithImages();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findPostsWithoutImages() {
        List<Post> posts = postAdminRepository.findPostsWithoutImages();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findAllByOrderByCreatedAtDesc() {
        List<Post> posts = postAdminRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public PostStatisticsDto getPostStatistics() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);

        return PostStatisticsDto.builder()
                .totalPosts(postAdminRepository.count())
                .postsLastWeek(postAdminRepository.countPostsLastWeek(oneWeekAgo))
                .postsLastMonth(postAdminRepository.countPostsLastWeek(oneMonthAgo))
                .totalLikes(postAdminRepository.countTotalLikes())
                .totalComments(postAdminRepository.countTotalComments())
                .postsWithImages(postAdminRepository.countPostsWithImages())
                .postsWithoutImages(postAdminRepository.count() - postAdminRepository.countPostsWithImages())
                .averageLikesPerPost(postAdminRepository.calculateAverageLikesPerPost())
                .averageCommentsPerPost((double) postAdminRepository.countTotalComments() / postAdminRepository.count())
                .hiddenPosts(postAdminRepository.countHiddenPosts())
                .build();
    }

    @Override
    public List<PostEngagementDto> getPostEngagementStats() {
        List<Post> posts = postAdminRepository.findAll();
        return posts.stream()
                .map(post -> PostEngagementDto.builder()
                        .postId(post.getId())
                        .userName(post.getUser().getUserName())
                        .likeCount(post.getLikes().size())
                        .commentCount(post.getComments().size())
                        .shareCount(0) // Nếu không có tính năng share
                        .engagementRate(calculateEngagementRate(post))
                        .createdAt(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DailyPostStatsDto> getDailyPostStats() {
        LocalDateTime startDate = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        List<Object[]> results = postAdminRepository.findDailyPostStatsRaw(startDate);

        // Chuyển đổi Object[] thành DailyPostStatsDto
        return results.stream().map(row -> new DailyPostStatsDto(
                ((java.sql.Date) row[0]).toLocalDate(), // chuyển đổi java.sql.Date thành LocalDate
                ((Number) row[1]).intValue(), // postCount
                ((Number) row[2]).intValue(), // likeCount
                ((Number) row[3]).intValue(), // commentCount
                ((Number) row[4]).doubleValue() // interactionPercentage
        )).collect(Collectors.toList());
    }



    private double calculateEngagementRate(Post post) {
        int totalInteractions = post.getLikes().size() + post.getComments().size();
        return (double) totalInteractions / postAdminRepository.count() * 100;
    }

    private PostAdminDto convertToDto(Post post) {
        return new PostAdminDto(
                post.getId(),
                post.getContent(),
                post.getUser().getUserName(),
                post.getCreatedAt(),
                post.getLikes().size(),
                post.getComments().size(),
                post.getDescription(),
                post.isHidden()
        );
    }
}