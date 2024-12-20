package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.model_enum.ReportStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long id; // ID của báo cáo
    private Long postId; // ID của bài post bị báo cáo
    private Long trackId; // ID của track
    private Long albumId; // ID của album
    private Long userId; // ID của người dùng thực hiện báo cáo
    private Long reportedId; // ID của người bị báo cáo
    private String reason; // Lý do báo cáo
    private LocalDate createDate; // Ngày tạo báo cáo
    private ReportStatus status; // Trạng thái báo cáo
    private String type;
    private String description;

}
