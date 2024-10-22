package org.example.library.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.OrderDetail;
import org.example.library.model.Track;
import org.example.library.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDto {

    private Long id;

    private String title;

    private String imagePlaylist;

    private String description;

    private String type;

    private LocalDate createDate;

    private boolean report;

    private LocalDate reportDate;

    private Set<Track> tracks;
}
