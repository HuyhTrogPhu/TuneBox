package org.example.library.service;


import org.apache.hc.client5.http.entity.mime.MultipartPart;
import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.dto.UserInformationDto;
import org.example.library.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.List;

public interface UserService {

    UserDto register(UserDto userDto, UserInformationDto userInformationDto, MultipartFile image);

    Optional<User> findById(Long userId);

    void forgotPassword(UserDto userDto);

    void resetPassword(String token, String newPassword);

    //  UserDto loginWithGoogle(String email , String name);
    UserDto loginWithGoogle(UserDto user);

    void changePassword(String email, String oldPassword, String newPassword);

    UserDto getUserById(Long userId);

    UserDto findUserById(Long userId);

    List<UserDto> findAllUser();

}
