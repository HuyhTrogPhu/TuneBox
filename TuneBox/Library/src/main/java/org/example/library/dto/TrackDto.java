package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate reportDate;
    private Long genreId;
    private Long user;
    private Long albumsId;
}
