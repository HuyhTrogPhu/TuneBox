package org.example.customer.API;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.library.dto.UserDto;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("/*")
@RestController
@RequestMapping("/User")
public class UserAPI {
    @Autowired
    private UserService userService;

    // REGISTER
    @PostMapping("/sign-up")
    public ResponseEntity<?> Register(@RequestBody UserDto user) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Đăng ký thành công");
            response.put("data", userService.Register(user));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Đăng ký thất bại");
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    // LOGIN
    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody UserDto user, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserDto loggedInUser = userService.Login(user);

            // Kiểm tra nếu người dùng tồn tại
            if (loggedInUser != null) {
                // Lưu ID người dùng vào session
                HttpSession session = request.getSession();
                Long userId = loggedInUser.getId(); // Lấy ID từ loggedInUser
                session.setAttribute("userId", userId); // Lưu ID vào session

                // Thiết lập thời gian session timeout (30 phút)
                session.setMaxInactiveInterval(60 * 60);

                response.put("status", true);
                response.put("message", "Đăng nhập thành công");
                response.put("data", loggedInUser);
            } else {
                throw new RuntimeException("Người dùng không tồn tại");
            }
        } catch (RuntimeException ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody UserDto user) {
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

    // Lấy thông tin người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    // Lấy thông tin người dùng hiện tại
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");
            if (userId != null) {
                UserDto userDto = userService.getUserById(userId);
                return ResponseEntity.ok(userDto);
            }
        }
        return ResponseEntity.status(401).body(null); // Trả về 401 nếu không tìm thấy thông tin người dùng
    }
}
