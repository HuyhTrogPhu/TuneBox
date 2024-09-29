package org.example.customer.API;

import org.example.library.dto.UserDto;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/User")
public class UserAPI {
    @Autowired
    private UserService UserService;


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


}