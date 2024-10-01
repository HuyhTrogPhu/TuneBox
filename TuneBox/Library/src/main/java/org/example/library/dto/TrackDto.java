package org.example.library.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.*;

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

    private String genreName;

    private String creatorName;

    private Long albumId;

    private Set<Playlist> playlists;

    private Set<Comment> comments;

    private Set<Like> likes;



}
