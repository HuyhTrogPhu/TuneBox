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

    private String genreName;

    private Long userId;

    private Long albumId;
    
    private String userName;

    private Set<Long> playlistIds;

    private Set<Long> comments;

    private Set<Long> likes;

    public TrackDto(Long id, String name, String imageTrack, byte[] trackFile, String description,
                    boolean status, LocalDate createDate, boolean report, Date reportDate,
                    Long genreId,  String genreName, Long userId, String userName, Long albumId,
                    Set<Long> playlistIds, Set<Long> comments, Set<Long> likes) {
        this.id = id;
        this.name = name;
        this.imageTrack = imageTrack;
        this.trackFile = trackFile;
        this.description = description;
        this.status = status;
        this.createDate = createDate;
        this.report = report;
        this.reportDate = reportDate;
        this.genreId = genreId;
        this.genreName = genreName;
        this.userId = userId;
        this.userName = userName;  // Đảm bảo trường này có
        this.albumId = albumId;
        this.playlistIds = playlistIds;
        this.comments = comments;
        this.likes = likes;
    }

}