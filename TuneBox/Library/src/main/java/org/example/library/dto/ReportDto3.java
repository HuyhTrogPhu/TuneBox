package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.model.Post;
import org.example.library.model_enum.ReportStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto3 {
    private Long id; // ID của báo cáo
    private ReportedDto post; // ID của người post bị báo cáo
    private Long userId; // ID của người dùng thực hiện báo cáo
    private String reason; // Lý do báo cáo
    private LocalDate createDate; // Ngày tạo báo cáo
    private ReportStatus status; // Trạng thái báo cáo
    private String description;
}
