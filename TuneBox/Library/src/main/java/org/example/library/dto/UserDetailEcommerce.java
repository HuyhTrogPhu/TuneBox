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
public class UserDetailEcommerce {

    private String name;

    private String gender;

    private String phone;

    private Date birthday;

    private String avatar;

    private String background;

    private String location;

    private String about;

    private String userName;

    private String email;
}
