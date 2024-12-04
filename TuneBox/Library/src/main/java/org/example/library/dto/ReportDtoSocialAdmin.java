package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model_enum.ReportStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDtoSocialAdmin {
    private Long id;
    private LocalDate createDate;
    private Long postId;
    private Long trackId;
    private String trackName;
    private Long albumId;
    private String albumName;
    private Long userId;
    private String userName;
    private Long UserReportedId;
    private String UserReportedName;
    private ReportStatus status;
    private LocalDateTime resolvedAt;
    private String description;
    private String type;
    private String reason;
}
