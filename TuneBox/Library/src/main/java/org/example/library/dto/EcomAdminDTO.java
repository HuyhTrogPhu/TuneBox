package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Role;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EcomAdminDTO {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String gender;

    private String phoneNumber;

    private String address;

    private String avatar;

    private Collection<Role> role;

}
