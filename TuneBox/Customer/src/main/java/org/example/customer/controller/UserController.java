package org.example.customer.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.library.dto.RequestSignUpModel;
import org.example.library.dto.UserDto;
import org.example.library.dto.UserInformationDto;
import org.example.library.model.RespondModel;
import org.example.library.repository.UserRepository;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody UserDto userDto, @RequestBody UserInformationDto userInformationDto,
                                      MultipartFile image) {

    }



}