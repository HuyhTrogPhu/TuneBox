package org.example.library.service;


import jakarta.transaction.Transactional;
import org.example.library.dto.*;
import org.example.library.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    UserDto register(UserDto userDto, UserInformationDto userInformationDto, MultipartFile image);

    // get user checkout information
    UserCheckOut getUserCheckoutInfo(Long userId);

    // get user avatar by userId
    String getUserAvatar(Long userId);

    // get profile user by userId
    UserProfileDto getProfileUserById(Long userId);

    Optional<UserFollowDto> getUserFollowById(Long userId);

//    void changePassword(String email, String oldPassword, String newPassword);

    List<UserDto> findAllUsers();

    List<UserMessageDTO> findAllReceiversExcludingSender(Long senderId);

    ProfileSettingDto getUserProfileSetting(Long userId);

    // Lấy số lượng followers của user
    Long getFollowersCount(Long userId);

    // Lấy số lượng following của user
    Long getFollowingCount(Long userId);

    // update userName by userId
    void updateUserName(Long userId, String newUserName);

    // update email in account page
    void updateEmail(Long userId, String newEmail);

    void updatePhoneNum(Long userId, String newPhone);

    // set password in account page
    void setPassword(Long userId, String newPassword);

    AccountSettingDto getAccountSetting(Long userId);

    // get all user in ecommerce customer page
    List<EcommerceUserDto> getAllUsersEcommerce();

    // get user details ecommerce customer page
    UserDetailEcommerce getUserDetailEcommerceAdmin(Long userId);

    // get user sell the most
    List<UserSell> getUserSellTheMost();

    // get top 1 user sell the most
    UserSell getTop1UserRevenueInfo();


    // get user buy the least
    List<UserSell> getUserBuyTheLeast();

    // get top 1 user buy the least
    UserSell getTop1UserBuyTheLeast();

    // get user not sell
    List<UserSell> getUserNotSell();

    List<UserSocialAdminDto> findAllUser();

    List<ListUserForMessageDto> findAllUserForMessage();

    @Transactional
    void updateBirthday(Long userId, Date newBirthday);

    @Transactional
    void updateGender(Long userId, String newGender);

    void updateUserInformation(Long userId, String name, String location, String about);

    // Update inspiredBy
    void updateInspiredBy(Long userId, List<Long> inspiredByIds);

    // Update talent
    void updateTalent(Long userId, List<Long> talentIds);

    // Update genre
    void updateGenre(Long userId, List<Long> genreIds);

    @Transactional
    void updateUserProfile(Long userId, UserUpdateRequest userUpdateRequest);


     List<SearchDto> searchPlaylist(String keyword);
     List<SearchDto> searchAlbum(String keyword);
     List<SearchDto> searchTrack(String keyword);
     List<SearchDto> searchUser(String keyword);

     long countUser();
    // get list user tagName
    List<UserTag> getUserTags(Long userId);
    UserSocialAdminDto findById(Long userId);

    Optional<User> findByIdUser(Long userId);

    List<User> findByReportTrue();
    public Map<LocalDate, Long> countUsersByDateRange(LocalDate startDate, LocalDate endDate);

    Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth);

    Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate);

    List<Object[]> getTop10MostFollowedUsers();

    List<UserSocialAdminDto> getUsersByDateRange(LocalDate startDate, LocalDate endDate);

    List<Map<String, Object>> getTop10UsersWithMostTracks(LocalDate startDate, LocalDate endDate);

    // list user sell by day
    List<UserSell> getUserSellTheMostDay(LocalDate date);

    // list user between days
    List<UserSell> getUserSellBetweenDate(LocalDate startDate, LocalDate endDate);

    // list user sell by week
    List<UserSell> getUserSellByWeek(LocalDate startDate);

    // list user between weeks
    List<UserSell> getUserSellBetweenWeek(LocalDate startDate, LocalDate endDate);

    // list user sell by month
    List<UserSell> getUserSellByMonth(int year, int month);

    // list user sell between months
    List<UserSell> getUserSellBetweenMonth(int year, int startMonth, int endMonth);

    // list user sell by year
    List<UserSell> getUserSellByYear(int year);

    // list user sell between years
    List<UserSell> getUserSellBetweenYear(int startYear, int endYear);
    void updateAvatar(Long userId, MultipartFile image);

    void updateBackground(Long userId, MultipartFile image);

    List<UserNameAvatarUsernameDto> getUsersNotFollowed(Long userId);

    <T> Optional<T> findByEmail(String email);

    Object createUser(String email);

    void banUser(Long userId);

    void unbanUser(Long userId);

    void checkAccountStatus(Long userId);
}
