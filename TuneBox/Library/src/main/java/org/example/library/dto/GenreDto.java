package org.example.library.dto;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Albums;
import org.example.library.model.Track;
import org.example.library.model.User;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private Long id;
    private String name;
    private Set<User> user;
    private Long trackId;
    private Long albumId;

    public GenreDto(Long id, String name){
        this.id = id;
        this.name = name;
    }


}
