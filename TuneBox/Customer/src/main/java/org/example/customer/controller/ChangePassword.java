package org.example.customer.controller;

import org.example.library.dto.ChangePasswordRequestDto;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/User")
public class ChangePassword {
    @Autowired
    private UserService UserService;


}
