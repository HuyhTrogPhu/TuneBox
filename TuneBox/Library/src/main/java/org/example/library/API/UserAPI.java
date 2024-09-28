package org.example.library.API;

import org.example.library.dto.UserDto;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserAPI {
    @Autowired
    private UserService UserSer;

    @PostMapping("/sign-up")
    public ResponseEntity<?> Register(@RequestBody UserDto user,
                                      @RequestBody String[] ListInspiredBy,
                                      @RequestBody String[] ListTalent,
                                      @RequestBody String[] GenreBy
    ) {
        Map<String, Object> response = new HashMap<>();


        try {
            response.put("status", true);
            response.put("message", "Đăng ký thành công");
            response.put("data", UserSer.Register(user,ListInspiredBy,ListTalent,GenreBy));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Đăng ký thất bại");
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
}
