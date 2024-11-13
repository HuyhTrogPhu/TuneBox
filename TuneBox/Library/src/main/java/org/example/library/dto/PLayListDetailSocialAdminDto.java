package org.example.library.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.angus.mail.handlers.image_gif;
import org.example.library.model.Track;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PLayListDetailSocialAdminDto {
    private Long id;
    private String title;
    private LocalDate createDate;
    private List<TrackDtoSocialAdmin> tracks;
    private String description;
    private Long LikeCount;
    private String image;
    private String Creator;
}
