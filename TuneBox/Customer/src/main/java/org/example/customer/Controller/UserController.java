package org.example.customer.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.model.RespondModel;
import org.example.library.repository.UserRepository;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    //LOGIN
    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody UserDto user) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserDto loggedInUser = userService.Login(user);
            response.put("status", true);
            response.put("message", "Đăng nhập thành công");
            System.out.println("Đang đăng nhập với: " + user.getUserName());
            System.out.println("Thông tin người dùng đăng nhập: " + loggedInUser);

            response.put("data", loggedInUser);
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }


    //FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody  UserDto user) {
        try {
            userService.ForgotPassword(user);
            return ResponseEntity.ok("Email reset password đã được gửi đến địa chỉ của bạn");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDto user) {
        try {
            userService.resetPassword(user.getToken(), user.getNewPassword());
            return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("Không tìm thấy người dùng với ID này.");
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok("Người dùng đã được xóa thành công.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Lỗi khi xóa người dùng: " + ex.getMessage());
        }
    }


    @GetMapping("/login/oauth2/success")
    public ResponseEntity<?> loginWithGoogle(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication is required");
        }

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // Kiểm tra oauth2User có phải null hay không
        if (oauth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 User is not found");
        }

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null || name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or name not found");
        }

        UserDto userDto = userService.loginWithGoogle(email, name);

        response.put("status", true);
        response.put("message", "Đăng nhập thành công");
        response.put("data", userDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> Register(@RequestBody RequestSignUpModel requestSignUpModel,
                                      HttpServletRequest request
    ) {
        RespondModel response = new RespondModel();
        try {
            response.setMessage("Đăng ký thành công");
            response.setData(userService.Register(requestSignUpModel));
            response.setStatus(true);
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", requestSignUpModel.getUserDto().getId());
        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setData(null);
            response.setStatus(false);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> GetUser(@PathVariable("id") Long UserId){
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", userService.findById(UserId));

        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Fail");
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/session")
    public ResponseEntity<?> getSessionData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object userId = session.getAttribute("userId");
            if (userId != null) {
                response.put("status", true);
                response.put("message", "Dữ liệu session được tìm thấy");
                response.put("data", userId);
            } else {
                response.put("status", false);
                response.put("message", "Không tìm thấy user ID trong session");
            }
        } else {
            response.put("status", false);
            response.put("message", "Session không tồn tại");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/check")
    public ResponseEntity<String> check(@RequestBody RequestSignUpModel requestSignUpModel) {
        try {
            userService.CheckLogin(requestSignUpModel);
            return ResponseEntity.ok("Check completed successfully");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason()); // Use getStatusCode()
        }
    }
    // Lấy thông tin người dùng hiện tại
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(@RequestParam("userId") Long userId) {
        if (userId != null) {
            UserDto userDto = userService.getUserById(userId);
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.status(401).body(null); // Trả về 401 nếu không tìm thấy thông tin người dùng
    }
    // Lấy thông tin người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@RequestParam("userId") Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }
}