package org.example.library.service.implement;

import org.example.library.dto.StatisticalUserDto;
import org.example.library.repository.UserAdminRepository;
import org.example.library.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserAdminRepository userAdminRepository;

    @Override
    public StatisticalUserDto getUserStatistics() {
        StatisticalUserDto dto = new StatisticalUserDto();

        // Basic stats
        long totalUsers = userAdminRepository.countTotalUsers();
        dto.setTotalUsers(totalUsers);
        dto.setReportedUsers(userAdminRepository.countReportedUsers());

        LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        dto.setNewUsersThisMonth(userAdminRepository.countNewUsersThisMonth(startOfMonth));

        dto.setReportedUserPercentage((double) dto.getReportedUsers() / totalUsers * 100);
        dto.setTotalFollowers(userAdminRepository.countTotalFollowers());
        dto.setTotalFollowing(userAdminRepository.countTotalFollowing());

        // Content creation stats
        dto.setUsersWithTracks(userAdminRepository.countUsersWithTracks());
        dto.setUsersWithAlbums(userAdminRepository.countUsersWithAlbums());

        // Engagement stats
        dto.setActiveCommenters(userAdminRepository.countActiveCommenters());
        dto.setActiveLikers(userAdminRepository.countActiveLikers());

        // User roles distribution
        Map<String, Long> usersByRole = userAdminRepository.getUserCountByRole()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
        dto.setUsersByRole(usersByRole);

        // User growth over time
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<StatisticalUserDto.MonthlyGrowth> growth = userAdminRepository.getUserGrowthByMonth(sixMonthsAgo)
                .stream()
                .map(arr -> {
                    StatisticalUserDto.MonthlyGrowth monthlyGrowth = new StatisticalUserDto.MonthlyGrowth();
                    monthlyGrowth.setMonth(Month.of((Integer) arr[0]).name());
                    monthlyGrowth.setUserCount((Long) arr[1]);
                    return monthlyGrowth;
                })
                .collect(Collectors.toList());
        dto.setUserGrowth(growth);

        // Influencer metrics (users with 1000+ followers)
        dto.setInfluencerCount(userAdminRepository.countInfluencers(1000));

        // Average posts per user
        dto.setAveragePostsPerUser(userAdminRepository.getAveragePostsPerUser());

        // Calculate engagement stats
        StatisticalUserDto.UserEngagementStats engagementStats = new StatisticalUserDto.UserEngagementStats();
        engagementStats.setEngagementRate((double) (dto.getActiveCommenters() + dto.getActiveLikers()) / totalUsers * 100);
        engagementStats.setAverageFollowersPerUser((double) dto.getTotalFollowers() / totalUsers);
        engagementStats.setAverageFollowingPerUser((double) dto.getTotalFollowing() / totalUsers);
        dto.setEngagementStats(engagementStats);

        return dto;
    }
}