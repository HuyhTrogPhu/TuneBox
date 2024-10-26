package org.example.library.service;

import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDto;
import org.example.library.model.Post;
import org.example.library.model_enum.ReportStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {
    ReportDto createReport(ReportDto reportDto);
    ReportDto getReportById(Long id);
    List<ReportDto> getAllReports();
    List<Report2Dto> getPendingReports(); // Thêm phương thức không phân trang
    Report2Dto resolveReport(Long reportId, boolean hidePost);
    Report2Dto getReport2ById(Long id);

    void restorePost(Long reportId);
    Report2Dto dismissReport(Long reportId, String reason);
}