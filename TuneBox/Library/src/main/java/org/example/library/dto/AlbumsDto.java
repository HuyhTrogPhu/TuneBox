package org.example.library.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.dto.TrackDto;

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
    private LocalDate createDate;
    private boolean report; //false
    private boolean status;
    private Long genreId;
    private Long creatorId;
    private Long albumStyleId;

    private Set<Long> tracks;



}
