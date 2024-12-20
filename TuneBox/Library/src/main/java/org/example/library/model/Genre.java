package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonBackReference
    @ManyToMany(mappedBy = "genre")
    @JsonIgnore
    private Set<User> user;

    @OneToMany(mappedBy = "genre",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Track> tracks;

    @OneToMany(mappedBy = "genre")
    @JsonManagedReference
    private Set<Albums> albums;


    public Genre(Long id, String name){
        this.id = id;
        this.name = name;
    }


}