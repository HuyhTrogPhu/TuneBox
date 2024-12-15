package org.example.library.service;

import org.example.library.dto.UserInformationDto;
import org.example.library.model.UserInformation;

import java.util.Optional;


public interface UserInforService{

    public Optional<UserInformation> findById(Long userId);
    public UserInformationDto updateUserInfor(Long userId, UserInformationDto UserInforDTO);
}
