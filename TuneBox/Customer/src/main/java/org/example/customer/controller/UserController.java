package org.example.customer.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.library.dto.*;
import org.example.library.model.Genre;
import org.example.library.model.InspiredBy;
import org.example.library.model.Talent;
import org.example.library.model.UserInformation;
import org.example.library.repository.UserRepository;
import org.example.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalentService talentService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private InspiredByService inspiredByService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserInformationService userInformationService;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam("userName") String userName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("talent") List<Long> talents,
            @RequestParam("genre") List<Long> genres,
            @RequestParam("inspiredBy") List<Long> inspiredBys,
            @RequestParam("image") MultipartFile image) {

        // Kiểm tra và ghi log thông tin nhận được
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(password);

        // Gọi service để thực hiện đăng ký
        UserDto userDto = new UserDto(userName, email, encodedPassword, talents, genres, inspiredBys);
        UserInformationDto userInformationDto = new UserInformationDto(name);

        UserDto registeredUser = userService.register(userDto, userInformationDto, image);
        return ResponseEntity.ok(registeredUser);
    }


    // get list talents
    @GetMapping("/list-talent")
    public ResponseEntity<List<Talent>> listTalent() {
        List<Talent> talentList = talentService.findAll();
        return ResponseEntity.ok(talentList);
    }

    // get list genres
    @GetMapping("/list-genre")
    public ResponseEntity<List<GenreDto>> listGenre() {
        List<Genre> genreList = genreService.findAll();
        List<GenreDto> genreDtoList = genreList.stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(genreDtoList);
    }

    // get list name genre
    @GetMapping("/listNameGenre")
    public ResponseEntity<List<GenreUserDto>> listNameGenre() {
        try {
            List<GenreUserDto> listNameGenres = genreService.findNameGenre();
            return ResponseEntity.ok(listNameGenres);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list inspired by
    @GetMapping("/list-inspired-by")
    public ResponseEntity<List<InspiredBy>> listInspiredBy() {
        List<InspiredBy> inspiredByList = inspiredByService.findAll();
        return ResponseEntity.ok(inspiredByList);
    }

    // Login
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
        Optional<UserLoginDto> optionalUser = userRepository.findByUserNameOrEmail(userLoginDto.getUserName(), userLoginDto.getEmail());

        if (optionalUser.isPresent()) {
            UserLoginDto user = optionalUser.get();

            if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                // Trả về userId thay vì toàn bộ thông tin user
                Long userId = user.getId();
                System.out.println("userId: " + userId);
                return ResponseEntity.ok(userId);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu không đúng");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên đăng nhập hoặc email không tồn tại");
        }
    }

    // Phương thức để lấy userId từ cookie
    private String getUserIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("userId".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ID not found in cookie");
    }

    // Get user avatar by userId
    @GetMapping("/{userId}/avatar")
    public ResponseEntity<String> getUserAvatar(@PathVariable Long userId) {
        try {
            String avatar = userService.getUserAvatar(userId);
            return ResponseEntity.ok(avatar);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting user avatar");
        }
    }

    // get user profile by userId
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDto> getProfileUser(@PathVariable Long userId) {
        try {
            UserProfileDto profileUser = userService.getProfileUserById(userId);
            return ResponseEntity.ok(profileUser);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<UserProfileDto>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // log-out
    @GetMapping("/log-out")
    public ResponseEntity<String> logOut(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie("userId", null);
            cookie.setMaxAge(0); // Thiết lập tuổi thọ cookie về 0 để xóa
            cookie.setPath("/");  // Đảm bảo xóa cookie cho toàn bộ domain
            response.addCookie(cookie);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging out");
        }
    }

    // get user in profile page
    @GetMapping("/{userId}/settingProfile")
    public ResponseEntity<ProfileSettingDto> getUserInformation(@PathVariable Long userId) {
        try {
            ProfileSettingDto profileUser = userService.getUserProfileSetting(userId);
            return ResponseEntity.ok(profileUser);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<ProfileSettingDto>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get user in account page
    @GetMapping("/{userId}/accountSetting")
    public ResponseEntity<AccountSettingDto> getUserAccount(@PathVariable Long userId) {
        try {
            AccountSettingDto userAccount = userService.getAccountSetting(userId);
            return ResponseEntity.ok(userAccount);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<AccountSettingDto>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get follower and following user by user id
    @GetMapping("/{userId}/followCount")
    public ResponseEntity<Map<String, Long>> getFollowCount(@PathVariable Long userId) {
        try {
            Long followersCount = userService.getFollowersCount(userId);
            Long followingCount = userService.getFollowingCount(userId);
            Map<String, Long> followCounts = new HashMap<>();
            followCounts.put("followers", followersCount);
            followCounts.put("following", followingCount);
            return ResponseEntity.ok(followCounts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // set password in account page
    @PutMapping("/{userId}/password")
    public ResponseEntity<String> setPassword(@PathVariable Long userId, @RequestParam String newPassword) {
        try {
            String encodedNewPassword = passwordEncoder.encode(newPassword);

            userService.setPassword(userId, encodedNewPassword);
            return ResponseEntity.ok("Mật khẩu đã thay đổi thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating password");
        }
    }


    // set username in account page
    @PutMapping("/{userId}/username")
    public ResponseEntity<String> setUsername(@PathVariable Long userId, @RequestParam String newUsername) {
        try {
            userService.updateUserName(userId, newUsername);
            return ResponseEntity.ok("Tên đăng nhập đã thay đổi thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating username");
        }
    }

    // set email in account page
    @PutMapping("/{userId}/email")
    public ResponseEntity<String> setEmail(@PathVariable Long userId, @RequestBody EmailUpdateRequest emailUpdateRequest) {
        try {
            userService.updateEmail(userId, emailUpdateRequest.getNewEmail()); // Sử dụng getNewEmail từ DTO
            return ResponseEntity.ok("Email đã thay đổi thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating email");
        }
    }
    // Cập nhật ngày sinh
    @PutMapping("/{userId}/birthday")
    public ResponseEntity<String> setBirthday(@PathVariable Long userId, @RequestBody String newBirthday) {
        try {
            // Làm sạch chuỗi ngày sinh bằng cách loại bỏ dấu nháy đôi
            newBirthday = newBirthday.replace("\"", "").trim();

            // Chuyển đổi chuỗi thành Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = dateFormat.parse(newBirthday);

            userService.updateBirthday(userId, birthday); // Cập nhật ngày sinh
            return ResponseEntity.ok("Ngày sinh đã thay đổi thành công");
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Ngày sinh không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating birthday");
        }
    }
    // Cập nhật giới tính
    @PutMapping("/{userId}/gender")
    public ResponseEntity<String> setGender(@PathVariable Long userId, @RequestBody String newGender) {
        try {
            String sanitizedGender = newGender.replace("\"", ""); // Xóa dấu ngoặc kép
            userService.updateGender(userId, sanitizedGender);
            return ResponseEntity.ok("Giới tính đã thay đổi thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating gender");
        }
    }

    //Cập nhật thông tin người dùng
    @PutMapping(value = "/{userId}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest userUpdateRequest) {
        System.out.println("Received request: " + userUpdateRequest);
        try {
            // Kiểm tra và cập nhật username nếu nó không null
            if (userUpdateRequest.getUserName() != null) {
                userService.updateUserName(userId, userUpdateRequest.getUserName());
            }

            // Cập nhật thông tin người dùng
            if (userUpdateRequest.getUserInformation() != null) {
                UserInfoUpdateDto userInfo = userUpdateRequest.getUserInformation();
                // Chỉ cập nhật name nếu nó không null
                if (userInfo.getName() != null) {
                    userService.updateUserInformation(userId, userInfo.getName(),
                            userInfo.getLocation() != null ? userInfo.getLocation() : null,
                            userInfo.getAbout() != null ? userInfo.getAbout() : null);
                }

                // Cập nhật location nếu nó không null
                if (userInfo.getLocation() != null) {
                    userService.updateUserInformation(userId,
                            userInfo.getName() != null ? userInfo.getName() : null,
                            userInfo.getLocation(),
                            userInfo.getAbout() != null ? userInfo.getAbout() : null);
                }

                // Cập nhật about nếu nó không null
                if (userInfo.getAbout() != null) {
                    userService.updateUserInformation(userId,
                            userInfo.getName() != null ? userInfo.getName() : null,
                            userInfo.getLocation() != null ? userInfo.getLocation() : null,
                            userInfo.getAbout());
                }
            }

            return ResponseEntity.ok("Cập nhật thông tin thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Để có thêm thông tin nếu có lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi cập nhật thông tin.");
        }
    }


}