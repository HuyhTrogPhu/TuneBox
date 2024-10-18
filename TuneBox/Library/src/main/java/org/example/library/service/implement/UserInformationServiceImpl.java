package org.example.library.service.implement;

import org.example.library.dto.ProfileSettingDto;
import org.example.library.repository.UserInformationRepository;
import org.example.library.service.UserInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInformationServiceImpl implements UserInformationService {

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Override
    public ProfileSettingDto getUserInformation(Long userId) {
        return userInformationRepository.findUserProfileByUserId(userId);
    }
}
