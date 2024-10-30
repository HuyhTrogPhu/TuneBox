package org.example.library.repository;

import org.example.library.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    @Modifying
    @Query("UPDATE UserInformation u SET u.birthDay = :newBirthday WHERE u.id = :userId")
    void updateBirthdayById(@Param("userId") Long userId, @Param("newBirthday") Date newBirthday);

    @Modifying
    @Query("UPDATE UserInformation u SET u.gender = :newGender WHERE u.id = :userId")
    void updateGenderById(@Param("userId") Long userId, @Param("newGender") String newGender);

}
