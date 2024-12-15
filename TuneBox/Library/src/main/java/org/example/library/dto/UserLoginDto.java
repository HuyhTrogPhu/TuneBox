package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.dto.RoleDto;
import org.example.library.model_enum.UserStatus;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDto {
    private Long id;
    private String email;
    private String userName;
    private String password;
    private RoleDto role;
    private UserStatus status;

    public UserLoginDto(Long id, String email, String userName, String password, RoleDto role, UserStatus status) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public UserLoginDto(Long id, String email, String userName, String password, Long roleId, String roleName) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.role = new RoleDto(roleId, roleName);
    }
}
