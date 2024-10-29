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

    private String trackFile;

    private String description;

    private boolean status;

    private LocalDate createDate;

    private boolean report;

    private Date reportDate;

    private Long genreId;

    private String genreName;

    private Long userId;

    private String userName;

    private Set<Long> albumIds;

    private Set<Long> playlistIds;

    private Set<Long> comments;

    private Set<Long> likes;

    public TrackDto(Long id, String name, String trackImage, byte[] bytes, String description, boolean status, LocalDate createDate, boolean report, Date reportDate, Long aLong, String s, Long aLong1, String s1, Long aLong2, Set<Long> playlist, Set<Long> comments, Set<Long> likes) {
    }
}