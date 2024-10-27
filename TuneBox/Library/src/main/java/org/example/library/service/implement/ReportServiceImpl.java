package org.example.library.service.implement;

import org.example.library.dto.ReportDto;

import org.example.library.mapper.ReportMapper;

import org.example.library.model.Report;
import org.example.library.model.User;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.ReportRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;  // Khai báo UserRepository để thao tác với User trong hệ thống

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public ReportDto createReport(ReportDto reportDto) {
        // Chuyển đổi từ DTO sang Entity
        Report report = reportMapper.toEntity(reportDto);

        report.setCreateDate(LocalDate.now());
        report.setStatus(ReportStatus.PENDING);

        // Lấy userId của người dùng hiện tại
        Long currentUserId = reportDto.getUserId(); // Giả sử bạn gửi userId từ front-end hoặc lấy từ token

        // Kiểm tra xem userId đã tồn tại hay chưa
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Set user vào report
        report.setUser(user);

        // Lưu báo cáo
        Report savedReport = reportRepository.save(report);

        // Chuyển đổi từ Entity sang DTO
        return reportMapper.toDto(savedReport);
    }

    @Override
    public ReportDto getReportById(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        return reportMapper.toDto(report);
    }

    @Override
    public List<ReportDto> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }
}
