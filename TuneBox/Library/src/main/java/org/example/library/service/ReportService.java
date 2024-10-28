package org.example.library.service;

import org.example.library.dto.ReportDto;

import java.util.List;

public interface ReportService {
    ReportDto createReport(ReportDto reportDto);
    ReportDto getReportById(Long id);
    List<ReportDto> getAllReports();
}
