package org.example.customer.API;

import org.example.library.dto.UserDto;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/User")
public class UserAPI {
    @Autowired
    private UserService UserSer;

    @PostMapping("/sign-up")
    public ResponseEntity<?> Register(@RequestBody UserDto user) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", true);
            response.put("message", "Đăng ký thành công");
            response.put("data", UserSer.Register(user));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Đăng ký thất bại");
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
}


