package org.example.library.service;


import jakarta.transaction.Transactional;
import org.example.library.dto.*;
import org.example.library.model.UserInformation;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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

    // get user in profile page
    ProfileSettingDto getUserProfileSetting(Long userId);

    // Lấy số lượng followers của user
    Long getFollowersCount(Long userId);

    // Lấy số lượng following của user
    Long getFollowingCount(Long userId);

    // update userName by userId
    void updateUserName(Long userId, String newUserName);

    // update email in account page
    void updateEmail(Long userId, String newEmail);

    // set password in account page
    void setPassword(Long userId, String newPassword);

    AccountSettingDto getAccountSetting(Long userId);

    // get all user in ecommerce customer page
    List<EcommerceUserDto> getAllUsersEcommerce();

    // get user details ecommerce customer page
    UserDetailEcommerce getUserDetailEcommerceAdmin(Long userId);

    List<UserDto> findAllUser();

    @Transactional
    void updateBirthday(Long userId, Date newBirthday);

    @Transactional
    void updateGender(Long userId, String newGender);

    void updateUserInformation(Long userId, String name, String location, String about);

    void updateUserProfile(Long userId, UserUpdateInspiredBytalentgenre userProfileUpdateRequest);
}
