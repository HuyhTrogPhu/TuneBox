
package org.example.library.service;



import jakarta.transaction.Transactional;
import org.example.library.dto.*;
import org.example.library.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;
import java.util.List;

public interface UserService {

    UserDto register(UserDto userDto, UserInformationDto userInformationDto, MultipartFile image);

    UserCheckOut getUserCheckoutInfo(Long userId);

    // get user avatar by userId
    String getUserAvatar(Long userId);

    // get profile user by userId
    UserProfileDto getProfileUserById(Long userId);

    Optional<UserFollowDto> getUserFollowById(Long userId);

//    void changePassword(String email, String oldPassword, String newPassword);

    List<UserDto> findAllUser();

    List<UserMessageDTO> findAllReceiversExcludingSender(Long senderId);

    ProfileSettingDto getUserProfileSetting(Long userId);

    // Lấy số lượng followers của user
    Long getFollowersCount(Long userId);

    // Lấy số lượng following của user
    Long getFollowingCount(Long userId);

    // update userName by userId
    void updateUserName(Long userId, String newUserName);

    // update email in account page
    void updateEmail(Long userId, String newEmail);

    // set password in account page
    void setPassword(Long userId, String newPassword);

    AccountSettingDto getAccountSetting(Long userId);

    // get all user in ecommerce customer page
    List<EcommerceUserDto> getAllUsersEcommerce();

    // get user details ecommerce customer page
    UserDetailEcommerce getUserDetailEcommerceAdmin(Long userId);

//    List<UserDto> findAllUser();

    @Transactional
    void updateBirthday(Long userId, Date newBirthday);

    @Transactional
    void updateGender(Long userId, String newGender);

    void updateUserInformation(Long userId, String name, String location, String about);

    // Update inspiredBy
    void updateInspiredBy(Long userId, List<Long> inspiredByIds);

    // Update talent
    void updateTalent(Long userId, List<Long> talentIds);

    // Update genre
    void updateGenre(Long userId, List<Long> genreIds);

    @Transactional
    void updateUserProfile(Long userId, UserUpdateRequest userUpdateRequest);


    public List<SearchDto> searchPlaylist(String keyword);
    public List<SearchDto> searchAlbum(String keyword);
    public List<SearchDto> searchTrack(String keyword);
    public List<SearchDto> searchUser(String keyword);

}
