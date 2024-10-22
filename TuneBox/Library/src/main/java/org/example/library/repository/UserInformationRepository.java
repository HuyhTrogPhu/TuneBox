package org.example.library.repository;

import org.example.library.dto.ProfileSettingDto;
import org.example.library.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {

    // get user information in profile page
    @Query("select new org.example.library.dto.ProfileSettingDto(ui.avatar, ui.name, ui.location, ui.gender, ui.birthDay, ui.about) " +
            "from UserInformation  ui join User u where u.id = :userId")
    ProfileSettingDto findUserProfileByUserId(@Param("userId") Long userId);
}
