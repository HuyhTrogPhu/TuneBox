package org.example.library.service;

import org.example.library.dto.ReportDto;

import java.util.List;

public interface ReportService {
    boolean checkReportExists(Long userId, Long postId, Long trackId, Long albumId, String type);

    ReportDto createReport(ReportDto reportDto);
    ReportDto getReportById(Long id);
    List<ReportDto> getAllReports();
}
