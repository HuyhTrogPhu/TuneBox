package org.example.customer.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.customer.config.JwtUtil;
import org.example.library.dto.*;
import org.example.library.model.Genre;
import org.example.library.model.InspiredBy;
import org.example.library.model.Talent;
import org.example.library.model.UserInformation;
import org.example.library.repository.UserRepository;
import org.example.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
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
    private OrderService orderService;

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
    @Autowired
    private JwtUtil jwtUtil;


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
        List<GenreDto> genreList = genreService.findAll();
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

                // Lấy tên vai trò từ đối tượng RoleDto
                String role = user.getRole() != null ? user.getRole().getName() : "Customer"; // Hoặc một vai trò mặc định khác

                // Tạo JWT token với username và role
                String jwtToken = jwtUtil.generateToken(user.getUserName(), role);
                Map<String, Object> response = new HashMap<>();
                response.put("userId", user.getId());
                response.put("token", jwtToken);
                return ResponseEntity.ok(response);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // log-out
    @GetMapping("/log-out")
    public ResponseEntity<String> logOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
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
    public ResponseEntity<Optional<UserFollowDto>> getFollowCount(@PathVariable Long userId) {
        try {
            Optional<UserFollowDto> followCount = userService.getUserFollowById(userId);
            return ResponseEntity.ok(followCount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Optional.empty());
        }
    }
    @PutMapping(value = "/{userId}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUserProfile(@PathVariable Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUserProfile(userId, userUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword) {
        try {
            // Gọi các phương thức tìm kiếm từ service
            List<SearchDto> userResults = userService.searchUser("%" + keyword + "%");
            List<SearchDto> trackResults = userService.searchTrack("%" + keyword + "%");
            List<SearchDto> albumResults = userService.searchAlbum("%" + keyword + "%");
            List<SearchDto> playlistResults = userService.searchPlaylist("%" + keyword + "%");

            // In ra dữ liệu kết quả tìm kiếm
            System.out.println("Kết quả tìm kiếm cho người dùng: " + userResults);
            System.out.println("Kết quả tìm kiếm cho bài hát: " + trackResults);
            System.out.println("Kết quả tìm kiếm cho album: " + albumResults);
            System.out.println("Kết quả tìm kiếm cho danh sách phát: " + playlistResults);

            // Tạo response chứa tất cả kết quả
            Map<String, Object> response = new HashMap<>();
            response.put("users", userResults);
            response.put("tracks", trackResults);
            response.put("albums", albumResults);
            response.put("playlists", playlistResults);

            // Trả về phản hồi thành công với kết quả tìm kiếm
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi lỗi
            return ResponseEntity.internalServerError().body("Error retrieving search results: " + e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/background")
    public ResponseEntity<?> updateBackground(@PathVariable Long userId, @RequestParam("image") MultipartFile image) {
        userService.updateBackground(userId, image);
        return ResponseEntity.ok("Background updated successfully.");
    }
    @PutMapping("/{userId}/avatar")
    public ResponseEntity<Void> updateAvatar(@PathVariable Long userId, @RequestParam("image") MultipartFile image) {
        userService.updateAvatar(userId, image);
        return ResponseEntity.ok().build();
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

    @GetMapping("/not-followed/{userId}")
    public ResponseEntity<List<UserNameAvatarUsernameDto>> getUsersNotFollowed(@PathVariable Long userId) {
        List<UserNameAvatarUsernameDto> users = userService.getUsersNotFollowed(userId);
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<List<ListUserForMessageDto>> getAllUsers() {
        List<ListUserForMessageDto> users = userService.findAllUserForMessage();
        return ResponseEntity.ok(users);
    }
    // get list orders by user id
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<UserIsInvoice>> getAllOrdersByUserId(@PathVariable Long userId) {
        try {
            List<UserIsInvoice> userIsInvoices = orderService.getOrderByUserId(userId);
            return ResponseEntity.ok(userIsInvoices);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

}