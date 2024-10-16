package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
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
    @JsonIgnore
    private Set<User> user;

    @OneToMany(mappedBy = "genre")
    @JsonIgnore
    private Set<Track> tracks;

    @OneToMany(mappedBy = "genre")
    @JsonIgnore
    private Set<Albums> albums;


    public Genre(Long id, String name){
        this.id = id;
        this.name = name;
    }


}
