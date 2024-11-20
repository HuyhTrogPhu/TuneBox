package org.example.library.service;

import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    boolean checkReportExists(Long userId, Long postId, Long trackId, Long albumId, String type);

    ReportDto createReport(ReportDto reportDto);

    ReportDto getReportById(Long id);

    List<ReportDto> getAllReports();


    Report2Dto resolveReport(Long reportId, boolean hidePost);

    Report2Dto getReport2ById(Long id);
    ReportDto updateApprove(Long id);
    ReportDto updateDenied(Long id);

    void restorePost(Long reportId);

//    Report2Dto dismissReport(Long reportId, String reason);

    Page<Report2Dto> getPendingReportsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Report2Dto> getAllPendingReports(Pageable pageable);
    Page<Report2Dto> getPendingReportsBySpecificDate(LocalDate specificDate, Pageable pageable);

    List<Report2Dto> dismissAllReports(Long postId, String reason);
}