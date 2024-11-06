package org.example.socialadmin.controller;

import org.example.library.dto.StatisticalUserDto;
import org.example.library.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StatisticalUserController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping("/statistics")
    public StatisticalUserDto getUserStatistics() {
        return adminUserService.getUserStatistics();
    }
}
