package org.example.library.service;

import org.example.library.dto.UserInformationDto;
import org.example.library.model.UserInformation;

import java.util.Optional;


public interface UserInforService{

    Optional<UserInformation> findById(Long userId);
    UserInformationDto updateUserInfor(Long userId, UserInformationDto UserInforDTO);
}
