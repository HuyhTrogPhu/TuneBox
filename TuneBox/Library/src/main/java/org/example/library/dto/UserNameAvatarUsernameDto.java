package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNameAvatarUsernameDto {
    private Long userId;
    private String userName;
    private String avatar;
    private String name;
}
