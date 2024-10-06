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
    private String trackImage;
    private byte[]  musicFile;
    private String description;
    private boolean status;
    private LocalDate createDate;
    private boolean report;
    private Date reportDate;
    private Long genreId;
    private Long creatorId;
    private Long albumId;
    private Set<Long> playlistsId;
    private Set<Long> commentsId;
    private Set<Long> likesId;

}
