package org.example.library.service;



import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.model.User;

import java.util.Optional;

public interface UserService {
    UserDto Register(RequestSignUpModel requestSignUpModel);
    Optional<User> findById(Long userId);
}
