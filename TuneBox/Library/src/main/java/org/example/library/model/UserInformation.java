package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    private Date birthDay;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String avatar;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String background;

    private String about;

    private String location;

    @OneToOne(mappedBy = "userInformation")
    @JsonIgnore
    private User user;

}