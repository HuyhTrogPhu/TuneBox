package org.example.library.service.implement;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.example.library.dto.*;
import org.example.library.mapper.UserMapper;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private InspiredByRepository inspiredByRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private Cloudinary cloudinary;


    @Override
    public UserDto register(UserDto userDto, UserInformationDto userInformationDto, MultipartFile image) {
        try {
            User user = new User();
            UserInformation userInformation = new UserInformation();

            userInformation.setName(userInformationDto.getName());

            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");
            userInformation.setAvatar(imageUrl);
            userInformation.setGender(userInformationDto.getGender());
            userInformation.setAbout(userInformationDto.getAbout());
            userInformation.setBackground(userInformationDto.getBackground());
            userInformation.setBirthDay(userInformationDto.getBirthDay());
            userInformation.setPhoneNumber(userInformationDto.getPhoneNumber());

            user.setUserInformation(userInformation);

            user.setRole(roleRepository.findByName("CUSTOMER"));
            user.setEmail(userDto.getEmail());
            user.setUserName(userDto.getUserName());
            user.setPassword(userDto.getPassword());
            user.setReport(false);
            user.setCreateDate(LocalDate.now());

            // Map InspiredBy từ danh sách ID trong DTO
            Set<InspiredBy> inspiredBySet = new HashSet<>();
            for (Long inspiredId : userDto.getInspiredBy()) {
                InspiredBy inspiredBy = inspiredByRepository.findById(inspiredId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inspired by not found"));
                inspiredBySet.add(inspiredBy);
            }
            user.setInspiredBy(inspiredBySet);

            // Map Talent từ danh sách ID trong DTO
            Set<Talent> talentSet = new HashSet<>();
            for (Long talentId : userDto.getTalent()) {
                Talent talent = talentRepository.findById(talentId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Talent not found"));
                talentSet.add(talent);
            }
            user.setTalent(talentSet);

            // Map Genre từ danh sách ID trong DTO
            Set<Genre> genreSet = new HashSet<>();
            for (Long genreId : userDto.getGenre()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));
                genreSet.add(genre);
            }
            user.setGenre(genreSet);

            userRepository.save(user);

            return UserMapper.mapToUserDto(user);

        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }

    }

    @Override
    public UserCheckOut getUserCheckoutInfo(Long userId) {
        return userRepository.getUserCheckOut(userId);
    }

    @Override
    public String getUserAvatar(Long userId) {
        return userRepository.findUserAvatarByUserId(userId);
    }

    @Override
    public UserProfileDto getProfileUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy thông tin cần thiết từ User
        UserProfileDto userProfile = new UserProfileDto();
        userProfile.setAvatar(user.getUserInformation().getAvatar());
        userProfile.setBackground(user.getUserInformation().getBackground());
        userProfile.setName(user.getUserInformation().getName());
        userProfile.setUserName(user.getUserName());

        // Tính toán số lượng followers và following
        int followersCount = user.getFollowers().size();  // Số lượng followers
        int followingCount = user.getFollowing().size();  // Số lượng following

        userProfile.setFollowersCount(followersCount);
        userProfile.setFollowingCount(followingCount);

        // Lấy danh sách talent, inspiredBy và genre
        List<String> talents = userRepository.findTalentByUserId(userId);
        List<String> inspiredBys = userRepository.findInspiredByByUserId(userId);
        List<String> genres = userRepository.findGenreByUserId(userId);

        // Set các danh sách này vào DTO
        userProfile.setTalent(talents);
        userProfile.setInspiredBy(inspiredBys);
        userProfile.setGenre(genres);

        return userProfile;
    }


    @Override
    public Optional<UserFollowDto> getUserFollowById(Long userId) {
        return userRepository.getFollowCount(userId);
    }

    @Override
    public ProfileSettingDto getUserProfileSetting(Long userId) {
        // Lấy thông tin cơ bản của người dùng
        ProfileSettingDto basicProfile = userRepository.findUserSettingProfile(userId);

        // Lấy danh sách inspiredBy, genre, và talent
        List<String> inspiredByList = userRepository.findInspiredByByUserId(userId);
        List<String> genreList = userRepository.findGenreByUserId(userId);
        List<String> talentList = userRepository.findTalentByUserId(userId);

        // Cập nhật danh sách vào DTO
        basicProfile.setInspiredBy(inspiredByList);
        basicProfile.setGenre(genreList);
        basicProfile.setTalent(talentList);

        return basicProfile;
    }

    @Override
    public Long getFollowersCount(Long userId) {
        return followRepository.countFollowersByUserId(userId);
    }

    @Override
    public Long getFollowingCount(Long userId) {
        return followRepository.countFollowingByUserId(userId);
    }

    @Override
    public void updateUserName(Long userId, String newUserName) {
        userRepository.updateUserNameById(userId, newUserName);
    }

    @Override
    public void updateEmail(Long userId, String newEmail) {
        userRepository.updateEmailById(userId, newEmail);
    }

    @Override
    public void setPassword(Long userId, String newPassword) {
        userRepository.updatePasswordById(userId, newPassword);
    }

    @Override
    public AccountSettingDto getAccountSetting(Long userId) {
        return userRepository.findAccountSettingProfile(userId);
    }

    @Override
    public List<EcommerceUserDto> getAllUsersEcommerce() {
        return userRepository.getAllUsersEcommerce();
    }

    @Override
    public UserDetailEcommerce getUserDetailEcommerceAdmin(Long userId) {
        return userRepository.getUserDetailEcommerceAdmin(userId);
    }
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    System.out.println("User ID: " + user.getId() + ", User Name: " + user.getUserName());
                    return new UserDto(user.getId(), user.getUserName());
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserSell> getUserSellTheMost() {
        return userRepository.getUserSellTheMost();
    }

    @Override
    public UserSell getTop1UserRevenueInfo() {
        List<UserSell> topUser = userRepository.getUserSellTheMost();
        if (!topUser.isEmpty()) {
            return topUser.get(0);
        }
        return null;
    }


    @Override
    public List<UserSell> getUserBuyTheLeast() {
        return userRepository.getUserBuyTheLeast();
    }

    @Override
    public UserSell getTop1UserBuyTheLeast() {
        List<UserSell> topUser = userRepository.getUserBuyTheLeast();
        if(!topUser.isEmpty()) {
            return topUser.get(0);
        }
        return null;
    }

    @Override
    public List<UserSell> getUserNotSell() {
        return userRepository.getUserNotSell();
    }



    @Override
    public List<UserDto> findAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = UserMapper.mapToUserDto(user);
            userDtos.add(userDto);
        }

        return userDtos;
    }

}