package org.example.library.service;


import org.example.library.dto.UserFollowDto;
import org.example.library.dto.UserProfileDto;
import org.example.library.dto.UserDto;
import org.example.library.dto.UserInformationDto;
import org.example.library.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.List;

public interface UserService {

    UserDto register(UserDto userDto, UserInformationDto userInformationDto, MultipartFile image);

    // get user avatar by userId
    String getUserAvatar(Long userId);

    // get profile user by userId
    UserProfileDto getProfileUserById(Long userId);

    Optional<UserFollowDto> getUserFollowById(Long userId);

    void changePassword(String email, String oldPassword, String newPassword);
    List<UserDto> findAllUser();

    UserDto getUserById(Long userId);
}
