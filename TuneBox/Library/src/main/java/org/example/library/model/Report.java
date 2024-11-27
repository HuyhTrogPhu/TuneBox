package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.library.model_enum.ReportStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate createDate = LocalDate.now();

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
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_reported", referencedColumnName = "user_id")
    private User reportedUser;

    private String reason;
//    private LocalDate createDate;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime resolvedAt;  // Add this field

    private String description;

    private String type;
}
