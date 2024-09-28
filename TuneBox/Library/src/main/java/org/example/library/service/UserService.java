package org.example.library.service;


import org.example.library.dto.BrandsDto;
import org.example.library.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto Register(UserDto user);

    UserDto Login(UserDto user);

    void ForgotPassword(UserDto user);

    void resetPassword(String token, String newPassword);
}