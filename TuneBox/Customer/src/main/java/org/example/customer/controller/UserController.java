package org.example.customer.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.library.dto.*;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.*;

import org.example.library.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtUtility jwtUtility;

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

    // get list inspired by
    @GetMapping("/list-inspired-by")
    public ResponseEntity<List<InspiredBy>> listInspiredBy() {
        List<InspiredBy> inspiredByList = inspiredByService.findAll();
        return ResponseEntity.ok(inspiredByList);
    }

    // login
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
        Optional<UserLoginDto> optionalUser = userRepository.findByUserNameOrEmail(userLoginDto.getUserName(), userLoginDto.getEmail());

        if (optionalUser.isPresent()) {
            UserLoginDto user = optionalUser.get();

            if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                // Tạo JWT token cho user
                String jwtToken = jwtUtility.generateToken(user.getUserName());

                // Thiết lập cookie cho userId
                Cookie cookie = new Cookie("userId", user.getId().toString());
                cookie.setMaxAge(24 * 60 * 60);
                cookie.setPath("/");
                response.addCookie(cookie);

                // Trả về phản hồi bao gồm token và userId
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("token", jwtToken);
                responseMap.put("userId", user.getId());

                System.out.println("Login successful");

                return ResponseEntity.ok(responseMap);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu không đúng");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên đăng nhập hoặc email không tồn tại");
        }
    }

    // log-out
    @GetMapping("/log-out")
    public ResponseEntity<String> logOut(HttpServletRequest request, HttpServletResponse response) {
        // Lấy JWT từ tiêu đề Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            // Thêm token vào blacklist hoặc lưu thông tin về việc đã log-out
            // Ví dụ: jwtBlacklistService.addTokenToBlacklist(jwt);

            // Xóa cookie userId
            Cookie cookie = new Cookie("userId", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token không hợp lệ");
        }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Trả về null hoặc thông điệp lỗi cụ thể
        }
    }


    // get user information in profile page
    @GetMapping("/{userId}/settingProfile")
    public ResponseEntity<ProfileSettingDto> getUserInformation(@PathVariable Long userId) {
        try {
            ProfileSettingDto userInfo = userService.getUserProfileSetting(userId);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<ProfileSettingDto>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get follower and following user by user id
    @GetMapping("/{userId}/followCount")
    public ResponseEntity<Optional<UserFollowDto>> getFollowCount(@PathVariable Long userId) {
        try {
            Optional<UserFollowDto> followCount = userService.getUserFollowById(userId);
            return ResponseEntity.ok(followCount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Optional.empty());
        }
    }


}