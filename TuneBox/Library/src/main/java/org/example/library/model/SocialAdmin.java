package org.example.library.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "social_admin")
public class SocialAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "social_admin_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String gender;

    private String phoneNumber;

    private String address;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "social_admin_role", joinColumns = @JoinColumn(name = "social_admin_id", referencedColumnName = "social_admin_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> role;

}
