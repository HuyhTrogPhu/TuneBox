package org.example.customer.controller;

import org.example.library.dto.UserDto;
import org.example.library.repository.UserRepository;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService UserService;

    @Autowired
    private UserRepository Repo;

    //REGISTER
    @PostMapping("/sign-up")
    public ResponseEntity<?> Register(@RequestBody UserDto user) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Đăng ký thành công");
            response.put("data", UserService.Register(user));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Đăng ký thất bại");
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    //LOGIN
    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody UserDto user) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserDto loggedInUser = UserService.Login(user);
            response.put("status", true);
            response.put("message", "Đăng nhập thành công");
            response.put("data", loggedInUser);
        } catch (RuntimeException ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }


    //FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody  UserDto user) {
        try {
            UserService.ForgotPassword(user);
            return ResponseEntity.ok("Email reset password đã được gửi đến địa chỉ của bạn");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDto user) {
        try {
            UserService.resetPassword(user.getToken(), user.getNewPassword());
            return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            if (!Repo.existsById(id)) {
                return ResponseEntity.badRequest().body("Không tìm thấy người dùng với ID này.");
            }
            Repo.deleteById(id);
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

        UserDto userDto = UserService.loginWithGoogle(email, name);

        response.put("status", true);
        response.put("message", "Đăng nhập thành công");
        response.put("data", userDto);

        return ResponseEntity.ok(response);
    }

}