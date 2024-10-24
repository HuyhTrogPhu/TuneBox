package org.example.library.repository;

import org.example.library.dto.ProfileSettingDto;
import org.example.library.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {


}
