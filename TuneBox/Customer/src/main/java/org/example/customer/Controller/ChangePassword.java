package org.example.customer.Controller;

import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/User")
public class ChangePassword {
    @Autowired
    private UserService UserService;


}
