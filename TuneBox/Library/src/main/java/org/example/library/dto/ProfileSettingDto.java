package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSettingDto {

    private String avatar;

    private String name;

    private String userName;

    private String location;

    private String about;

    private List<String> inspiredBy;

    private List<String> genre;

    private List<String> talent;

    public ProfileSettingDto(String avatar, String name, String userName, String location, String about) {
        this.avatar = avatar;
        this.name = name;
        this.userName = userName;
        this.location = location;
        this.about = about;
    }
}
