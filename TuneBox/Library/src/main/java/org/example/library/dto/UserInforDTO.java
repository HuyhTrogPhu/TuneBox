package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Number;
import org.example.library.model.User;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInforDTO {
    private Long id;

    private String firstName;

    private String gender;

    private String phoneNumber;

    private Date birthDate;

    private String avatar;

    private String background;

    private String about;

    private Number number;

    private User user;
}
