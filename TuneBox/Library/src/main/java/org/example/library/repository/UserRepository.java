package org.example.library.repository;

import org.example.library.dto.*;
import org.example.library.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // list username
    @Query("SELECT u.userName FROM User u")
    List<String> findAllUserNames();

    // list email
    @Query("SELECT u.email FROM User u")
    List<String> findAllUserEmails();

    User findByUserName(String username);
    // get user by username or email
    @Query("SELECT new org.example.library.dto.UserLoginDto(u.id, u.email, u.userName, u.password, new org.example.library.dto.RoleDto(r.id, r.name)) " +
            "FROM User u JOIN u.role r WHERE u.userName = :userName OR u.email = :email")
    Optional<UserLoginDto> findByUserNameOrEmail(String userName, String email);

    // get user check out info
    @Query("select new org.example.library.dto.UserCheckOut(u.id, u.email, u.userName) " +
            "from User u WHERE u.id = :userId")
    UserCheckOut getUserCheckOut(@Param("userId") Long userId);

    // get followers and following by user id
    @Query("select size(u.followers), size(u.following) from User u where u.id = :userId")
    Optional<UserFollowDto> getFollowCount(@Param("userId") Long userId);

    // get avatar user
    @Query("SELECT ui.avatar FROM UserInformation ui JOIN ui.user u WHERE u.id = :userId")
    String findUserAvatarByUserId(@Param("userId") Long userId);

    // Lấy thông tin cơ bản của người dùng (avatar, background, name, userName)
    @Query("select new org.example.library.dto.UserProfileDto(ui.avatar, ui.background, ui.name, u.userName) " +
            "from UserInformation ui join ui.user u where u.id = :userId")
    UserProfileDto findUserProfileByUserId(@Param("userId") Long userId);

    @Query("select new org.example.library.dto.AccountSettingDto(u.email, ui.birthDay, ui.gender) " +
            "from User u join u.userInformation ui where u.id = :userId")
    AccountSettingDto findAccountSettingProfile(@Param("userId") Long userId);

    // Lấy thông tin cơ bản của người dùng (avatar, name, userName, location, about)
    @Query("select new org.example.library.dto.ProfileSettingDto(ui.avatar, ui.name, u.userName, ui.location, ui.about) " +
            "from UserInformation ui join ui.user u where u.id = :userId")
    ProfileSettingDto findUserSettingProfile(@Param("userId") Long userId);

    // Lấy danh sách talent của người dùng
    @Query("select ta.name from User u join u.talent ta where u.id = :userId")
    List<String> findTalentByUserId(@Param("userId") Long userId);

    // Lấy danh sách inspiredBy của người dùng
    @Query("select ins.name from User u join u.inspiredBy ins where u.id = :userId")
    List<String> findInspiredByByUserId(@Param("userId") Long userId);

    // Lấy danh sách genre của người dùng
    @Query("select ge.name from User u join u.genre ge where u.id = :userId")
    List<String> findGenreByUserId(@Param("userId") Long userId);

    // update userName
    @Modifying
    @Query("update User u set u.userName = :newUserName where u.id = :userId")
    void updateUserNameById(@Param("userId") Long userId, @Param("newUserName") String newUserName);

    // update email
    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.id = :userId")
    void updateEmailById(@Param("userId") Long userId, @Param("newEmail") String newEmail);


    // Cập nhật mật khẩu mới cho user
    @Modifying
    @Query("update User u set u.password = :newPassword where u.id = :userId")
    void updatePasswordById(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    // get user ecommerce admin
    @Query("select new org.example.library.dto.EcommerceUserDto(u.id, u.userName, u.email, COUNT(o.id), SUM(o.totalPrice)) " +
            "from User u " +
            "join u.orderList o " +
            "group by u.id, u.userName, u.email")
    List<EcommerceUserDto> getAllUsersEcommerce();

    // get user detail ecommerce admin
    @Query("SELECT new org.example.library.dto.UserDetailEcommerce( ui.name, ui.gender, ui.phoneNumber, ui.birthDay, ui.avatar," +
            "ui.background, ui.location, ui.about, u.userName, u.email)" +
            "from UserInformation ui join ui.user u where u.id = :userId")
    UserDetailEcommerce getUserDetailEcommerceAdmin(@Param("userId") Long userId);

    boolean existsByUserName(String userName);

    // get list user sell the most
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    List<UserSell> getUserSellTheMost();

    // get top 1 user sell the most
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    Optional<UserSell> getTopUserBuyTheMost();


    // get user revenue of day
    @Query("select sum(o.totalPrice) " +
            "from Order o " +
            "where o.user.id = ?1 and o.orderDate = CURRENT_DATE")
    Double getTotalRevenueTodayByTopUser(Long userId);


    // get user revenue of week
    @Query("select sum(o.totalPrice) " +
            "from Order o " +
            "where o.user.id = ?1 and function('YEAR', o.orderDate) = function('YEAR', CURRENT_DATE) " +
            "and function('WEEK', o.orderDate) = function('WEEK', CURRENT_DATE)")
    Double getTotalRevenueThisWeekByTopUser(Long userId);


    // get user revenue of month
    @Query("select sum(o.totalPrice) " +
            "from Order o " +
            "where o.user.id = ?1 and function('YEAR', o.orderDate) = function('YEAR', CURRENT_DATE) " +
            "and function('MONTH', o.orderDate) = function('MONTH', CURRENT_DATE)")
    Double getTotalRevenueThisMonthByTopUser(Long userId);


    // get user revenue of year
    @Query("select sum(o.totalPrice) " +
            "from Order o " +
            "where o.user.id = ?1 and function('YEAR', o.orderDate) = function('YEAR', CURRENT_DATE)")
    Double getTotalRevenueThisYearByTopUser(Long userId);



    // get list user buy the least
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) asc")
    List<UserSell> getUserBuyTheLeast();


    // get top 1 user buy the least
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) asc")
    Optional<UserSell> getTopUserBuyTheLeast();


    // get user not sell
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, cast(0 as long), cast(0.0 as double)) " +
            "from UserInformation ui join ui.user u left join u.orderList o " +
            "where o.id is null")
    List<UserSell> getUserNotSell();


    // search
    @Query("SELECT new org.example.library.dto.SearchDto(us.id, ui.avatar, ui.name) " +
            "from UserInformation ui join ui.user us where ui.name like :keyword")
    List<SearchDto> searchUser(@Param("keyword") String keyword);

    @Query("SELECT new org.example.library.dto.SearchDto(t.id, t.id, t.name, t.description, t.trackImage, t.creator.userName) " +
            "from Track t where t.name like :keyword or t.description like :keyword or t.genre.name like :keyword or t.creator.userName like :keyword")
    List<SearchDto> searchTrack(@Param("keyword") String keyword);

    @Query("SELECT new org.example.library.dto.SearchDto(a.id, a.title, a.description, a.albumImage, a.creator.userName) " +
            "from Albums a where a.title like :keyword or a.description like :keyword or a.genre.name like :keyword or a.albumStyle.name like :keyword or a.creator.userName like :keyword")
    List<SearchDto> searchAlbum(@Param("keyword") String keyword);

    @Query("SELECT new org.example.library.dto.SearchDto(p.id, p.title, p.imagePlaylist, p.creator.userName) " +
            "from Playlist p where p.title like :keyword or p.description like :keyword or p.type like :keyword or p.creator.userName like :keyword")
    List<SearchDto> searchPlaylist(@Param("keyword") String keyword);


    // get list user sell by day
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "where o.orderDate = :date " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    List<UserSell> getUserSellTheMostOfDay(@Param("date") LocalDate date);

    // get list user sell from date to date
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "where o.orderDate BETWEEN :startDate AND :endDate " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    List<UserSell> getUserSellBetweenDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // get list user sell by week
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "where YEARWEEK(o.orderDate, 1) = YEARWEEK(:date, 1) " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    List<UserSell> getUserSellByWeek(@Param("date") LocalDate date);


    // get list user sell between weeks
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "where (YEAR(o.orderDate) = YEAR(:startDate) and WEEK(o.orderDate) >= WEEK(:startDate)) " +
            "   or (YEAR(o.orderDate) = YEAR(:endDate) and WEEK(o.orderDate) <= WEEK(:endDate)) " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    List<UserSell> getUserSellFromWeekToWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    // get list user sell by month
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "where YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    List<UserSell> getUserSellsByMonth(@Param("year") int year, @Param("month") int month);


    // get list user sell between month
    @Query("select new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, count(o.id), sum(o.totalPrice)) " +
            "from UserInformation ui join ui.user u join u.orderList o " +
            "where YEAR(o.orderDate) = :year AND MONTH(o.orderDate) BETWEEN :startMonth AND :endMonth " +
            "group by u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "order by sum(o.totalPrice) desc")
    List<UserSell> getUserSellsBetweenMonths(@Param("year") int year, @Param("startMonth") int startMonth, @Param("endMonth") int endMonth);

    // get list user sell by year
    @Query("SELECT new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, COUNT(o.id), SUM(o.totalPrice)) " +
            "FROM UserInformation ui JOIN ui.user u JOIN u.orderList o " +
            "WHERE YEAR(o.orderDate) = :year " +
            "GROUP BY u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "ORDER BY SUM(o.totalPrice) DESC")
    List<UserSell> getUserSellByYear(@Param("year") int year);


    // get list user sell between year
    @Query("SELECT new org.example.library.dto.UserSell(u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email, COUNT(o.id), SUM(o.totalPrice)) " +
            "FROM UserInformation ui JOIN ui.user u JOIN u.orderList o " +
            "WHERE YEAR(o.orderDate) BETWEEN :startYear AND :endYear " +
            "GROUP BY u.id, ui.name, ui.phoneNumber, u.userName, ui.location, u.email " +
            "ORDER BY SUM(o.totalPrice) DESC")
    List<UserSell> getUserSellBetweenYears(@Param("startYear") int startYear, @Param("endYear") int endYear);


}
