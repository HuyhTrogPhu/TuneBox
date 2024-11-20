package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.model_enum.ReportStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report2Dto {
    private Long id; // ID của báo cáo
    private PostThisIdDto post; // ID của bài post bị báo cáo
    private String reporterId; // ID của người dùng thực hiện báo cáo
    private String reason; // Lý do báo cáo
    private LocalDate createDate; // Ngày tạo báo cáo
    private ReportStatus status; // Trạng thái báo cáo
    private String description;
    private List<ReportDetailDto> reportDetails;

    private int reportCount; // Đếm số lượng báo cáo (đã được khởi tạo trong mapReportsToDto)

    // Setter cho reportCount đã có sẵn
    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

}
