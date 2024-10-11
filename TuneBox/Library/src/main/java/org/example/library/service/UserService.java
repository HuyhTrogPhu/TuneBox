package org.example.library.service;



import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.model.User;

import java.util.Optional;
import java.util.List;

public interface UserService {
    void CheckLogin(RequestSignUpModel requestSignUpModel);
    UserDto Register(RequestSignUpModel requestSignUpModel);
    Optional<User> findById(Long userId);

    void ForgotPassword(UserDto user);

    void resetPassword(String token, String newPassword);

    //    UserDto loginWithGoogle(String email , String name);
    UserDto Login(UserDto user);

    void changePassword(String email, String oldPassword, String newPassword);

    UserDto getUserById(Long userId);

    User findUserById(Long userId);
}