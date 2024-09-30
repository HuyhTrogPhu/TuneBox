package org.example.customer.API;

import org.example.library.dto.RequestSignUpModel;

import org.example.library.model.RespondModel;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;



@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserAPI {
    @Autowired
    private UserService UserSer;

    @PostMapping("/sign-up")
    public ResponseEntity<?> Register(@RequestBody RequestSignUpModel requestSignUpModel
                                      ) {

        RespondModel response = new RespondModel();


        try {

            response.setMessage("Đăng ký thành công");
            response.setData(UserSer.Register(requestSignUpModel));
            response.setStatus(true);
        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            response.setData(null);
            response.setStatus(false);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> GetUser(@PathVariable("id") Long UserId){
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", true);
            response.put("message", "Succesfull");
            response.put("data", UserSer.findById(UserId));
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Fail");
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }
}
