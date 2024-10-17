package org.example.library.service;

import org.example.library.dto.ProfileSettingDto;

public interface UserInformationService {

    // get user information in profile page
    ProfileSettingDto getUserInformation(Long userId);
}
