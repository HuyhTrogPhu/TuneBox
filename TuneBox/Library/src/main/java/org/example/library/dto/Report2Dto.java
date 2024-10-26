package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.model_enum.ReportStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report2Dto {
    private Long id; // ID của báo cáo
    private PostThisIdDto post; // ID của bài post bị báo cáo
    private Long reporterId; // ID của người dùng thực hiện báo cáo
    private String reason; // Lý do báo cáo
    private LocalDate createDate; // Ngày tạo báo cáo
    private ReportStatus status; // Trạng thái báo cáo
    private String description;
}
