package org.example.ecommerceadmin.controller;

import org.example.library.dto.EcomAdminDTO;
import org.example.library.dto.UserDto;
import org.example.library.service.EcomAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/e-comAdmin/account")
public class EcomAdminAccount {
    @Autowired
    private EcomAdminService serviceAdmin;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/log-in")
    public ResponseEntity<?> login(@RequestBody EcomAdminDTO admin) {
        Map<String, Object> response = new HashMap<>();

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        try {
            EcomAdminDTO loggedIn = serviceAdmin.login(admin);

            response.put("status", true);
            response.put("message", "Đăng nhập thành công");
            response.put("data", loggedIn);
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<?> ADDbyPostMan(@RequestBody EcomAdminDTO admin) {
        Map<String, Object> response = new HashMap<>();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        try {
            EcomAdminDTO Added = serviceAdmin.AddAdmin(admin);

            response.put("status", true);
            response.put("message", "add thanh cong");
            response.put("data", Added);
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
}
