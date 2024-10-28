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
public class UserProfileDto {

    private String avatar;
    private String background;
    private String name;
    private String userName;
    private List<String> talent;
    private List<String> inspiredBy;
    private List<String> genre;

    // Thêm các trường mới để lưu trữ số lượng followers và following
    private int followersCount;  // Số lượng người theo dõi
    private int followingCount;  // Số lượng người đang theo dõi

    public UserProfileDto(String avatar, String background, String name, String userName) {
        this.avatar = avatar;
        this.background = background;
        this.name = name;
        this.userName = userName;
    }
}

