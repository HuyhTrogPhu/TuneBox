package org.example.ecommerceadmin.controller;


import org.example.library.dto.EcommerceUserDto;
import org.example.library.dto.UserDetailEcommerce;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/e-customer")
public class UserController {

    @Autowired
    private UserService userService;

    // get all users
    @GetMapping("/users")
    public ResponseEntity<List<EcommerceUserDto>> getAllUsers() {
        try {
            List<EcommerceUserDto> users = userService.getAllUsersEcommerce();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDetailEcommerce> getUserById(@PathVariable Long userId) {
        try {
            UserDetailEcommerce userDetailEcommerce = userService.getUserDetailEcommerceAdmin(userId);
            return ResponseEntity.ok(userDetailEcommerce);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
