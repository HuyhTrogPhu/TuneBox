package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.AlbumStyle;
import org.example.library.model.Genre;
import org.example.library.model.Track;
import org.example.library.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumsDto {

    private Long id;
    private String title;
    private String albumImage;
    private String description;
    private Date releaseDate;
    private LocalDate createDate;
    private boolean report;
    private Date reportDate;
    private String status;
    private Genre genre;
    private User creator;
    private Set<Track> tracks;
    private AlbumStyle albumStyle;
}
