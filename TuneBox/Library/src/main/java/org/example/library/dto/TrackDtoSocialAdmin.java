package org.example.library.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Track;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDtoSocialAdmin {
    private Long id;
    private String title;
    private LocalDateTime createDate;
    private boolean report;
    private Date reportDate;
    private String userNickname;
    private int LikeCount;
}
