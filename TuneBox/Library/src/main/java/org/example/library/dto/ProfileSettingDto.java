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
public class ProfileSettingDto {

    private String avatar;

    private String name;

    private String location;

    private String gender;

    private Date birthDate;

    private String about;
}
