package org.example.library.service.implement;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.library.dto.*;
import org.example.library.mapper.UserMapper;
import org.example.library.model.*;
import org.example.library.model_enum.UserStatus;
import org.example.library.repository.*;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private AlbumsRepository albumsRepository;

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
    public void updateUserName(Long userId, String userName) {
        // Tìm người dùng theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Cập nhật tên người dùng
        if (userName != null && !userName.isEmpty()) {
            user.setUserName(userName);
        }

        // Lưu lại thông tin đã cập nhật
        userRepository.save(user);
    }


    @Override
    @Transactional
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
    public List<UserSocialAdminDto> findAllUser() {
        List<User> users = userRepository.findAll();
        List<UserSocialAdminDto> userDtos = new ArrayList<>();
            System.out.println("In find all");
        for (User user : users) {
            List<Track> listTracks = new ArrayList<>(user.getTracks());
            List<TrackDtoSocialAdmin> trackDtos = listTracks.stream()
                    .map(track -> new TrackDtoSocialAdmin(
                            track.getId(),
                            track.getName(),
                            track.getCreateDate(),
                            track.isReport(),
                            track.getReportDate(),
                            track.getCreator().getUserName(),
                            track.getLikes().size()
                    ))
                    .collect(Collectors.toList());
            long likeCount = likeRepository.countByUserId(user.getId());
            long commentCount = commentRepository.countByUserId(user.getId());
            //   List<Report> listReport = new ArrayList<>(user.getReports());
            long postCount = user.getPosts().size();
            long odersCount =user.getOrderList().size();
            long friendCount = friendRepository.countByUserId(user.getId());
            long Albumcount = albumsRepository.countByCreatorId(user.getId());
            UserSocialAdminDto userDto = new UserSocialAdminDto(
                    user.getId(),
                    user.getEmail(),
                    user.getUserName(),
                    user.getCreateDate(),
                    user.getFollowing().size(),
                    user.getFollowers().size(),

                    postCount,
                    trackDtos,
                    odersCount,
                    user.getUserInformation(),
                    commentCount,
                    likeCount,
                    friendCount,
                    Albumcount,

                    user.getReportCount(),
                    user.getGenre().stream()
                                    .map(genre -> {
                                        Genre temp = new Genre();
                                        temp.setId(genre.getId());
                                        temp.setName(genre.getName());
                                        return temp;
                                    })
                                    .collect(Collectors.toList()),
                    user.getInspiredBy().stream()
                            .map(inspiredBy -> {
                                InspiredBy temp = new InspiredBy();
                                temp.setId(inspiredBy.getId());
                                temp.setName(inspiredBy.getName());
                                return temp;
                            })
                            .collect(Collectors.toList()),
                    user.getTalent().stream()
                            .map(talent -> {
                                Talent temp = new Talent();
                                temp.setId(talent.getId());
                                temp.setName(talent.getName());
                                return temp;
                            })
                            .collect(Collectors.toList()),
                    user.getStatus());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<ListUserForMessageDto> findAllUserForMessage() {
        // Lấy danh sách tất cả User từ repository
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    ListUserForMessageDto dto = new ListUserForMessageDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUserName());
                    dto.setNickName(user.getUserInformation().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = UserMapper.mapToUserDto(user);
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Override
    @Transactional
    public void updateBirthday(Long userId, Date birthday) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.getUserInformation().setBirthDay(birthday); // Cập nhật ngày sinh
        userRepository.save(user);
    }
    @Override
    public void updatePhoneNum(Long userId, String newPhone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.getUserInformation().setPhoneNumber(newPhone);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateGender(Long userId, String newGender) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserInformation userInfo = user.getUserInformation();
        userInfo.setGender(newGender.trim()); // Cắt bỏ khoảng trắng nếu có
        userInformationRepository.save(userInfo); // Lưu lại thay đổi
    }

    @Override
    public void updateUserInformation(Long userId, String name, String location, String about) {
        // Tìm người dùng theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        UserInformation userInfo = user.getUserInformation();
        if (userInfo != null) {
            userInfo.setName(name);
            userInfo.setLocation(location);
            userInfo.setAbout(about);
        } else {
            throw new RuntimeException("Thông tin người dùng không tồn tại");
        }
        // Lưu lại thông tin đã cập nhật
        userRepository.save(user);
    }
    //updateInspiredBy
    public void updateInspiredBy(Long userId, List<Long> inspiredByIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Clear current inspiredBy and set new values
        user.getInspiredBy().clear();
        for (Long id : inspiredByIds) {
            InspiredBy inspiredBy = inspiredByRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("InspiredBy not found for ID: " + id));
            user.getInspiredBy().add(inspiredBy);
        }

        userRepository.save(user); // Save changes
    }
//updateTalent
    public void updateTalent(Long userId, List<Long> talentIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Clear current talents and set new values
        user.getTalent().clear();
        for (Long id : talentIds) {
            Talent talent = talentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Talent not found for ID: " + id));
            user.getTalent().add(talent);
        }

        userRepository.save(user); // Save changes
    }
//updateGenre
    public void updateGenre(Long userId, List<Long> genreIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Clear current genres and set new values
        user.getGenre().clear();
        for (Long id : genreIds) {
            Genre genre = genreRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Genre not found for ID: " + id));
            user.getGenre().add(genre);
        }

        userRepository.save(user); // Save changes
    }
    @Transactional
    @Override
    public void updateUserProfile(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Cập nhật tên người dùng
        if (userUpdateRequest.getUserName() != null && !userUpdateRequest.getUserName().isEmpty()) {
            user.setUserName(userUpdateRequest.getUserName());
        }

        // Cập nhật thông tin cá nhân từ UserInfoUpdateDto
        UserInfoUpdateDto userInfoUpdate = userUpdateRequest.getUserInformation();
        if (userInfoUpdate != null) {
            UserInformation userInfo = user.getUserInformation();
            if (userInfo != null) {
                if (userInfoUpdate.getName() != null) {
                    userInfo.setName(userInfoUpdate.getName());
                }
                if (userInfoUpdate.getLocation() != null) {
                    userInfo.setLocation(userInfoUpdate.getLocation());
                }
                if (userInfoUpdate.getAbout() != null) {
                    userInfo.setAbout(userInfoUpdate.getAbout());
                }
            }
        }

        // Cập nhật inspiredBy
        if (userUpdateRequest.getInspiredBy() != null) {
            updateInspiredBy(userId, userUpdateRequest.getInspiredBy());
        }

        // Cập nhật talent
        if (userUpdateRequest.getTalent() != null) {
            updateTalent(userId, userUpdateRequest.getTalent());
        }

        // Cập nhật genre
        if (userUpdateRequest.getGenre() != null) {
            updateGenre(userId, userUpdateRequest.getGenre());
        }

        // Lưu lại thông tin đã cập nhật
        userRepository.save(user);
    }


    @Override
    public List<SearchDto> searchUser(String keyword) {
        return userRepository.searchUser(keyword);
    }

    @Override
    public List<UserSell> getUserSellTheMostDay(LocalDate date) {
        return userRepository.getUserSellTheMostOfDay(date);
    }

    @Override
    public List<UserSell> getUserSellBetweenDate(LocalDate startDate, LocalDate endDate) {
        return userRepository.getUserSellBetweenDate(startDate, endDate);
    }

    @Override
    public List<UserSell> getUserSellByWeek(LocalDate startDate) {
        return userRepository.getUserSellByWeek(startDate);
    }

    @Override
    public List<UserSell> getUserSellBetweenWeek(LocalDate startDate, LocalDate endDate) {
        return userRepository.getUserSellFromWeekToWeek(startDate, endDate);
    }

    @Override
    public List<UserSell> getUserSellByMonth(int year, int month) {
        return userRepository.getUserSellsByMonth(year, month);
    }

    @Override
    public List<UserSell> getUserSellBetweenMonth(int year, int startMonth, int endMonth) {
        return userRepository.getUserSellsBetweenMonths(year, startMonth, endMonth);
    }

    @Override
    public List<UserSell> getUserSellByYear(int year) {
        return userRepository.getUserSellByYear(year);
    }

    @Override
    public List<UserSell> getUserSellBetweenYear(int startYear, int endYear) {
        return userRepository.getUserSellBetweenYears(startYear, endYear);
    }


    @Override
    public void updateAvatar(Long userId, MultipartFile image) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            UserInformation userInformation = user.getUserInformation();
            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");
            userInformation.setAvatar(imageUrl);
            userRepository.save(user); // Lưu thay đổi
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary", e);
        }
    }

        @Override
    public List<SearchDto> searchTrack(String keyword) {
        return userRepository.searchTrack(keyword);
    }

    @Override
    public List<SearchDto> searchAlbum(String keyword) {
        return userRepository.searchAlbum(keyword);
    }

    @Override
    public List<SearchDto> searchPlaylist(String keyword) {
        return userRepository.searchPlaylist(keyword);
    }

    @Override
    public List<UserMessageDTO> findAllReceiversExcludingSender(Long senderId) {
        List<User> users = userRepository.findAll();
        // Lọc bỏ người gửi
        users = users.stream()
                .filter(user -> !user.getId().equals(senderId)) // loại bỏ người dùng đã đăng nhập
                .collect(Collectors.toList());
        // Chuyển đổi danh sách người dùng thành danh sách UserMessageDTO
        List<UserMessageDTO> userMessageDTOs = users.stream()
                .map(user -> new UserMessageDTO(user.getId(), user.getId(), senderId)) // Gán id và senderId
                .collect(Collectors.toList());
        return userMessageDTOs;
    }


    @Override
    public void updateBackground(Long userId, MultipartFile image) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            UserInformation userInformation = user.getUserInformation();

            // Tải lên hình ảnh lên Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            // Cập nhật URL hình nền
            userInformation.setBackground(imageUrl);
            userRepository.save(user); // Lưu thay đổi
        } catch (IOException e) {
            throw new RuntimeException("Error uploading background image to Cloudinary", e);
        }
    }
    @Override
    public List<UserNameAvatarUsernameDto> getUsersNotFollowed(Long userId) {
        List<User> usersNotFollowed = userRepository.findUsersNotFollowedBy(userId);

        return usersNotFollowed.stream().map(user -> {
            String avatar = user.getUserInformation() != null ? user.getUserInformation().getAvatar() : null;
            return new UserNameAvatarUsernameDto(
                    user.getId(),
                    user.getUserName(),
                    avatar,
                    user.getUserInformation() != null ? user.getUserInformation().getName() : null
            );
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findOptionalByEmail(email);
    }

    @Override
    public User createUser(String email) {
        User newUser = new User();
        newUser.setEmail(email);
        // Bạn có thể thiết lập các thuộc tính khác nếu có
        return userRepository.save(newUser); // Lưu người dùng mới vào cơ sở dữ liệu
    }

    @Override
    public long countUser() {
        return userRepository.countByIdNotNull();
    }



    @Override
    public UserSocialAdminDto findById(Long userId) {
        User user =  userRepository.findById(userId).get();
        List<Track> listTracks = new ArrayList<>(user.getTracks());
        List<TrackDtoSocialAdmin> trackDtos = listTracks.stream()
                .map(track -> new TrackDtoSocialAdmin(
                        track.getId(),
                        track.getName(),
                        track.getCreateDate(),
                        track.isReport(),
                        track.getReportDate(),
                        track.getCreator().getUserName(),
                        track.getLikes().size()
                ))
                .collect(Collectors.toList());

        long postCount = user.getPosts().size();
        long odersCount =user.getOrderList().size();
        long likeCount = likeRepository.countByUserId(user.getId());
        long commentCount = commentRepository.countByUserId(user.getId());

        //      List<Report> listReport = new ArrayList<>(user.getReports());
        long friendCount = friendRepository.countByUserId(user.getId());
        long Albumcount = albumsRepository.countByCreatorId(user.getId());

        UserSocialAdminDto userDto = new UserSocialAdminDto(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getCreateDate(),
                user.getFollowing().size(),

                user.getFollowers().size(),
                postCount,
                trackDtos,
                odersCount,
                user.getUserInformation(),
                commentCount,
                likeCount,
                friendCount,
                Albumcount,
                user.getReportCount(),
                user.getGenre().stream()
                        .map(genre -> {
                            Genre temp = new Genre();
                            temp.setId(genre.getId());
                            temp.setName(genre.getName());
                            return temp;
                        })
                        .collect(Collectors.toList()),
                user.getInspiredBy().stream()
                        .map(inspiredBy -> {
                            InspiredBy temp = new InspiredBy();
                            temp.setId(inspiredBy.getId());
                            temp.setName(inspiredBy.getName());
                            return temp;
                        })
                        .collect(Collectors.toList()),
                user.getTalent().stream()
                        .map(talent -> {
                            Talent temp = new Talent();
                            temp.setId(talent.getId());
                            temp.setName(talent.getName());
                            return temp;
                        })
                        .collect(Collectors.toList()),
        user.getStatus());

        return userDto;
    }

    @Override
    public Optional<User> findByIdUser(Long userId) {
        return userRepository.findById(userId);
    }


    @Override
    public List<User> findByReportTrue(){
        return userRepository.findByReportTrue();

    };
    //static for Social admin
    public Map<LocalDate, Long> countUsersByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Long count = userRepository.countByCreateDate(currentDate);
            userCountMap.put(currentDate, count);
            currentDate = currentDate.plusDays(1);
        }
        return userCountMap;
    }

    @Override
    public List<UserSocialAdminDto> getUsersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<User> users =  userRepository.findAllByCreateDateBetween(startDate, endDate);
        List<UserSocialAdminDto> userDtos = new ArrayList<>();
        for (User user : users) {
            List<Track> listTracks = new ArrayList<>(user.getTracks());
            List<TrackDtoSocialAdmin> trackDtos = listTracks.stream()
                    .map(track -> new TrackDtoSocialAdmin(
                            track.getId(),
                            track.getName(),
                            track.getCreateDate(),
                            track.isReport(),
                            track.getReportDate(),
                            track.getCreator().getUserName(),
                            track.getLikes().size()
                    ))
                    .collect(Collectors.toList());
            long likeCount = likeRepository.countByUserId(user.getId());
         long commentCount = commentRepository.countByUserId(user.getId());
 //           List<Report> listReport = new ArrayList<>(user.getReports());
            long postCount = user.getPosts().size();
            long odersCount =user.getOrderList().size();
            long friendCount = friendRepository.countByUserId(user.getId());
            long Albumcount = albumsRepository.countByCreatorId(user.getId());

            UserSocialAdminDto userDto = new UserSocialAdminDto(
                    user.getId(),
                    user.getEmail(),
                    user.getUserName(),
                    user.getCreateDate(),
                    user.getFollowers().size(),
                    user.getFollowing().size(),
                    postCount,
                    trackDtos,
                    odersCount,
                    user.getUserInformation(),
                    commentCount,
                    likeCount,
                    friendCount,
                    Albumcount,
                    user.getReportCount(),
                    user.getGenre().stream()
                            .map(genre -> {
                                Genre temp = new Genre();
                                temp.setId(genre.getId());
                                temp.setName(genre.getName());
                                return temp;
                            })
                            .collect(Collectors.toList()),
                    user.getInspiredBy().stream()
                            .map(inspiredBy -> {
                                InspiredBy temp = new InspiredBy();
                                temp.setId(inspiredBy.getId());
                                temp.setName(inspiredBy.getName());
                                return temp;
                            })
                            .collect(Collectors.toList()),
                    user.getTalent().stream()
                            .map(talent -> {
                                Talent temp = new Talent();
                                temp.setId(talent.getId());
                                temp.setName(talent.getName());
                                return temp;
                            })
                            .collect(Collectors.toList()),
                    user.getStatus());
                    //                 listReport

            userDtos.add(userDto);
        }

        return userDtos;
    }

    public Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        LocalDate currentWeekStart = startDate.with(DayOfWeek.MONDAY);

        while (!currentWeekStart.isAfter(endDate)) {
            LocalDate currentWeekEnd = currentWeekStart.with(DayOfWeek.SUNDAY);
            Long count = userRepository.countByCreateDateBetween(currentWeekStart, currentWeekEnd);
            userCountMap.put(currentWeekStart, count);
            currentWeekStart = currentWeekStart.plusWeeks(1);
        }
        return userCountMap;
    }
    public Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth) {
        Map<YearMonth, Long> userCountMap = new HashMap<>();
        YearMonth currentMonth = startMonth;

        while (!currentMonth.isAfter(endMonth)) {
            LocalDate monthStart = currentMonth.atDay(1);
            LocalDate monthEnd = currentMonth.atEndOfMonth();
            Long count = userRepository.countByCreateDateBetween(monthStart, monthEnd);
            userCountMap.put(currentMonth, count);
            currentMonth = currentMonth.plusMonths(1);
        }

        return userCountMap;
    }
    public List<Object[]> getTop10MostFollowedUsers() {
        return userRepository.findTop10MostFollowedUsers();
    }
    public List<Map<String, Object>> getTop10UsersWithMostTracks(LocalDate startDate, LocalDate endDate) {
        // Lấy dữ liệu từ repository
        LocalDateTime startDateTime = startDate.atStartOfDay(); // Từ 00:00:00 của ngày bắt đầu
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> result = userRepository.findTop10UsersWithMostTracks(startDateTime, endDateTime);

        // Chuyển đổi dữ liệu từ Object[] sang Map
        return result.stream()
                .map(record -> Map.of(
                        "userId", record[0],
                        "trackCount", record[1]
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        user.setStatus(UserStatus.BANNED);
        userRepository.save(user);
    }


    @Override
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void checkAccountStatus(Long userId) {
        // Tìm người dùng theo ID
        Optional<User> userOptional = userRepository.findById(userId);

        // Nếu người dùng không tồn tại, ném ngoại lệ
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = userOptional.get();

        // Kiểm tra trạng thái bị khóa (banned)
        if (user.getStatus()==UserStatus.BANNED) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your account has been banned. Please contact support."
            );
        }
    }
    @Override
    public List<UserTag> getUserTags(Long userId) {
        return userRepository.findUserTags(userId);
    }

}