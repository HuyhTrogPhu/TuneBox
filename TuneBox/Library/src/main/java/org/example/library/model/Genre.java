package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "genre_id")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "genre")
    private Set<User> user;

    @OneToMany(mappedBy = "genre")
    private Set<Track> tracks;

    @OneToMany(mappedBy = "genre")
    private Set<Albums> albums;


}
