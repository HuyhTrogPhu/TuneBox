package org.example.library.service;

import org.example.library.dto.UserInforDTO;
import org.example.library.model.UserInformation;
import org.springframework.stereotype.Service;
import java.util.Optional;


public interface UserInforService{
    public UserInforDTO updateUserInfor(Long userId, UserInforDTO UserInforDTO);
    public Optional<UserInformation> findById(Long userId);
}
