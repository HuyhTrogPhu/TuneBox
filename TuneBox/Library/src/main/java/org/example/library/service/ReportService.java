package org.example.library.service;

import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDto;
import org.example.library.dto.ReportDtoSocialAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    boolean checkReportExists(Long userId, Long postId, Long trackId, Long albumId, String type);

    ReportDto createReport(ReportDto reportDto);

    ReportDto getReportById(Long id);

    List<ReportDto> getAllReports();

    List<Report2Dto> getPendingReports(); // Thêm phương thức không phân trang

    Report2Dto resolveReport(Long reportId, boolean hidePost);

    Report2Dto getReport2ById(Long id);
    ReportDto updateApprove(Long id);
    ReportDto updateDenied(Long id);
    Page<ReportDtoSocialAdmin> findAllReportsWithTracks(Pageable pageable);
    Page<ReportDtoSocialAdmin> findAllReportsWithAlbum(Pageable pageable);
    Page<ReportDtoSocialAdmin> findAllReportsWithPost(Pageable pageable);
     ReportDtoSocialAdmin findById(Long id);
    void restorePost(Long reportId);
    Report2Dto dismissReport(Long reportId, String reason);
}