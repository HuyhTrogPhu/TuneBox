package org.example.customer.controller;

import org.example.customer.config.JwtUtil;
import org.example.library.model.User;
import org.example.library.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private final JwtDecoder jwtDecoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtDecoder jwtDecoder, JwtUtil jwtUtil, UserService userService) {
        this.jwtDecoder = jwtDecoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/api/auth/google")
    public ResponseEntity<Map<String, Object>> loginWithGoogle(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        Map<String, Object> response = new HashMap<>();

        try {
            Jwt jwt = jwtDecoder.decode(idToken);
            String email = jwt.getClaimAsString("email");
            String name = jwt.getClaimAsString("name");

            // Kiểm tra xem người dùng đã tồn tại chưa
            Optional<User> userOptional = userService.findByEmail(email);

            if (userOptional.isPresent()) {
                // Nếu người dùng đã có, trả về token và thông tin userId
                User user = userOptional.get();
                String token = jwtUtil.generateToken(user.getEmail(), "USER");

                response.put("token", token);
                response.put("userId", user.getId());
                response.put("userExists", true);  // Đã có tài khoản
            } else {
                // Nếu người dùng chưa có, tạo mới tài khoản

                String token = jwtUtil.generateToken(email, "USER");

                response.put("token", token);

                response.put("userExists", false);  // Chưa có tài khoản

            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }
    }
}