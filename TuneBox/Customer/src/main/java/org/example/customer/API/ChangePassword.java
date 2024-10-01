package org.example.customer.API;

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

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestdto) {
        try {
            UserService.changePassword(changePasswordRequestdto.getEmail(),
                    changePasswordRequestdto.getOldPassword(),
                    changePasswordRequestdto.getNewPassword());
            return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
