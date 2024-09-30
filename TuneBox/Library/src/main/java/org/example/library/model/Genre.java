package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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


    public Genre(Long id, String name){
        this.id = id;
        this.name = name;
    }


}
