package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String gender;

    private String phoneNumber;

    private Date birthDate;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String avatar;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String background;

    private String about;



    @OneToOne(mappedBy = "userInformation")
    private User user;


}
