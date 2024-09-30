package org.example.library.model;

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

    @ManyToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> users;
}
