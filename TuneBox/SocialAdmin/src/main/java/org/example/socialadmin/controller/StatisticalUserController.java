package org.example.socialadmin.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StatisticalUserController {

}
