package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostReportDto {
    private Long id; // ID của báo cáo
    private String content;
    private String reporter; // Người báo cáo
    private Long reportedPostId; // ID của bài viết bị báo cáo
    private String reason; // Lý do báo cáo
    private LocalDateTime dateReported; // Ngày báo cáo
    private boolean sensitive; // phần này của post report

}
