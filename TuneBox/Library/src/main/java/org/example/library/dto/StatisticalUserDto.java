package org.example.library.dto;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class StatisticalUserDto {
    private long totalUsers;
    private long reportedUsers;
    private long newUsersThisMonth;
    private double reportedUserPercentage;
    private long totalFollowers;
    private long totalFollowing;

    // New fields
    private long usersWithTracks;
    private long usersWithAlbums;
    private long activeCommenters;
    private long activeLikers;
    private Map<String, Long> usersByRole;
    private List<MonthlyGrowth> userGrowth;
    private long influencerCount;
    private double averagePostsPerUser;
    private UserEngagementStats engagementStats;

    @Data
    public static class MonthlyGrowth {
        private String month;
        private long userCount;
    }

    @Data
    public static class UserEngagementStats {
        private double engagementRate;
        private long activeUsersLast30Days;
        private double averageFollowersPerUser;
        private double averageFollowingPerUser;
    }
}