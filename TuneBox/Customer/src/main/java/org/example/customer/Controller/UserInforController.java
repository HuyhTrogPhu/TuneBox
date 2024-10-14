package org.example.customer.controller;

import org.example.library.dto.UserDto;
import org.example.library.dto.UserInforDTO;
import org.example.library.model.RespondModel;
import org.example.library.service.UserInforService;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/userInfor")
public class UserInforController {
    @Autowired
    private UserService UserService;

    @Autowired
    private UserInforService UserInforService;

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserInforDTO UserInforDTO) {
        RespondModel response = new RespondModel();

        try {
            UserInforDTO updatedUser = UserInforService.updateUserInfor(id, UserInforDTO);
            response.setMessage("Cập nhật thành công");
            response.setData(updatedUser);
            response.setStatus(true);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setData(null);
            response.setStatus(false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> GetUser(@PathVariable("id") Long UserId){
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", UserInforService.findById(UserId));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Fail");
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }

}
