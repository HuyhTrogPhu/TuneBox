package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumSocialDto {
    private Long id;
    private String title;
    private LocalDate createDate;
    private boolean report;
    private Date reportDate;
    private List<TrackDtoSocialAdmin> tracks;
    private String Creator;
    private String description;
}
