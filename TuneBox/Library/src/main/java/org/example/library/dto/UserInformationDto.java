package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformationDto {

    private Long userInformationId;

    private String name;

    private String gender;

    private String phoneNumber;

    private Date birthDay;

    private String avatar;

    private String background;

    private String about;

    private Long userId;

    public UserInformationDto(Long id, String name, String gender, String phoneNumber, Date birthDay, String avatar, String background, String about) {
        this.userInformationId = id;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.avatar = avatar;
        this.background = background;
        this.about = about;
        this.userId = null;
    }
}
