package org.example.library.dto;


import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Albums;
import org.example.library.model.Genre;
import org.example.library.model.Track;
import org.example.library.model.User;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private Long id;

    private String name;

    private Set<User> user;

    private Set<Track> tracks;

    private Set<Albums> albums;

    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }

}
