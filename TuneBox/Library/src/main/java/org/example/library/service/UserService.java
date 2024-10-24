package org.example.library.service;


import org.example.library.dto.*;
import org.springframework.web.multipart.MultipartFile;

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
}
