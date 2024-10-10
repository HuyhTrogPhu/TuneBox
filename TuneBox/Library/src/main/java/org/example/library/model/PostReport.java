package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_report")
public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String reporter; // Người báo cáo
    private Long reportedPostId; // ID của bài viết bị báo cáo
    private String reason; // Lý do báo cáo

    @Column(name = "date_reported")
    private LocalDateTime dateReported; // Ngày báo cáo

}
