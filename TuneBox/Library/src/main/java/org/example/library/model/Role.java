package org.example.library.model;

import aj.org.objectweb.asm.commons.Remapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<SocialAdmin> socialAdmins;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<EcommerceAdmin> ecommerceAdmins;


}
