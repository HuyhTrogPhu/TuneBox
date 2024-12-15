package org.example.library.repository;

import org.example.library.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface UserAdminRepository extends CrudRepository<User, Long> {
    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.report = true")
    long countReportedUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.createDate >= :startOfMonth")
    long countNewUsersThisMonth(LocalDate startOfMonth);

    @Query("SELECT SUM(size(u.followers)) FROM User u")
    long countTotalFollowers();

    @Query("SELECT SUM(size(u.following)) FROM User u")
    long countTotalFollowing();

    // New queries
    @Query("SELECT COUNT(DISTINCT u) FROM User u WHERE SIZE(u.tracks) > 0")
    long countUsersWithTracks();

    @Query("SELECT COUNT(DISTINCT u) FROM User u WHERE SIZE(u.albums) > 0")
    long countUsersWithAlbums();

    @Query("SELECT COUNT(DISTINCT u) FROM User u WHERE SIZE(u.comments) > 0")
    long countActiveCommenters();

    @Query("SELECT COUNT(DISTINCT u) FROM User u WHERE SIZE(u.likes) > 0")
    long countActiveLikers();

    @Query("SELECT u.role.name, COUNT(u) FROM User u GROUP BY u.role.name")
    List<Object[]> getUserCountByRole();

    @Query("SELECT COUNT(u) FROM User u WHERE SIZE(u.followers) >= :minFollowers")
    long countInfluencers(int minFollowers);

    @Query(value = "SELECT MONTH(u.create_date) AS month, COUNT(*) FROM users u WHERE u.create_date >= :startDate GROUP BY MONTH(u.create_date) ORDER BY month", nativeQuery = true)
    List<Object[]> getUserGrowthByMonth(LocalDate startDate);
    
    @Query("SELECT COALESCE(AVG(size(u.posts)), 0) FROM User u")
    double getAveragePostsPerUser();

}