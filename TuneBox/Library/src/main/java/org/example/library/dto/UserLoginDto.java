package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    private Long id;

    private String email;

    private String userName;

    private String password;

    private RoleDto role;

    public UserLoginDto(Long id,String email,String userName,String password) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}
