package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String imageTrack;

    private byte[] trackFile;

    private String description;

    private boolean status;

    private LocalDate createDate;

    private boolean report;

    private Date reportDate;

    private Long genreId;

    private Long userId;

    private Long albumId;

    private Set<Long> playlistIds;

    private Set<Long> comments;

    private Set<Long> likes;



}