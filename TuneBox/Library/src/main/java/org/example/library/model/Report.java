package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.model_enum.ReportStatus;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = true)
    private Track track;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = true)
    private Albums album;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String reason;
    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String type;
}
