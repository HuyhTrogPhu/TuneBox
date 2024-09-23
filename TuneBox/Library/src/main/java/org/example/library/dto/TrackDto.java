package org.example.library.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Albums;
import org.example.library.model.Genre;
import org.example.library.model.Playlist;
import org.example.library.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDto {

    private Long id;

    private String name;

    private String trackImage;

    private String description;

    private boolean status;

    private LocalDate createDate;

    private boolean report;

    private Date reportDate;

    private Genre genre;

    private User creator;

    private Albums albums;

    private Set<Playlist> playlists;
    
}
