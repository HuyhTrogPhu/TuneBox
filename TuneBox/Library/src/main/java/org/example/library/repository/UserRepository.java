package org.example.library.repository;

import org.example.library.dto.*;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // get user by username or email
    @Query("SELECT new org.example.library.dto.UserLoginDto(u.id, u.email, u.userName, u.password) " +
            "FROM User u WHERE u.userName = :userName OR u.email = :email")
    Optional<UserLoginDto> findByUserNameOrEmail(String userName, String email);

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
    @Query("update User u set u.email = :newEmail where u.id = :userId")
    void updateEmailById(@Param("userId") Long userId, @Param("newEmail") String newEmail);

    // Cập nhật mật khẩu mới cho user
    @Modifying
    @Query("update User u set u.password = :newPassword where u.id = :userId")
    void updatePasswordById(@Param("userId") Long userId, @Param("newPassword") String newPassword);

}
