package org.example.library.repository;

import org.example.library.dto.UserProfileDto;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // get user by username or email
    Optional<User> findByUserNameOrEmail(String userName, String email);

    // get avatar user
    @Query("SELECT ui.avatar FROM UserInformation ui JOIN ui.user u WHERE u.id = :userId")
    String findUserAvatarByUserId(@Param("userId") Long userId);

    // Lấy thông tin cơ bản của người dùng (avatar, background, name, userName)
    @Query("select new org.example.library.dto.UserProfileDto(ui.avatar, ui.background, ui.name, u.userName) " +
            "from UserInformation ui join ui.user u where u.id = :userId")
    UserProfileDto findUserProfileByUserId(@Param("userId") Long userId);

    // Lấy danh sách talent của người dùng
    @Query("select ta.name from User u join u.talent ta where u.id = :userId")
    List<String> findTalentByUserId(@Param("userId") Long userId);

    // Lấy danh sách inspiredBy của người dùng
    @Query("select ins.name from User u join u.inspiredBy ins where u.id = :userId")
    List<String> findInspiredByByUserId(@Param("userId") Long userId);

    // Lấy danh sách genre của người dùng
    @Query("select ge.name from User u join u.genre ge where u.id = :userId")
    List<String> findGenreByUserId(@Param("userId") Long userId);

}
