package org.example.library.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;

    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users;

    @JsonBackReference
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<SocialAdmin> socialAdmins;

    @JsonBackReference
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<EcommerceAdmin> ecommerceAdmins;


}
