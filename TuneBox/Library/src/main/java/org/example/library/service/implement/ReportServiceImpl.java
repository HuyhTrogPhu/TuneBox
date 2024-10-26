package org.example.library.service.implement;

import org.example.library.Exception.ResourceNotFoundException;
import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDto;

import org.example.library.mapper.ReportMapper;

import org.example.library.model.Post;
import org.example.library.model.Report;
import org.example.library.model.User;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.ReportRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private PostService postService;

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
    public Report2Dto getReport2ById(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        return reportMapper.toReport2Dto(report);
    }


    @Override
    public List<ReportDto> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report2Dto> getPendingReports() {
        List<Report> pendingReports = reportRepository.findByStatus(ReportStatus.PENDING);
        return pendingReports.stream()
                .map(reportMapper::toReport2Dto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Report2Dto resolveReport(Long reportId, boolean hidePost) {
        Report report = reportRepository.findById(reportId)
                .filter(r -> r.getStatus() == ReportStatus.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Pending report not found with id: " + reportId));

        Post post = report.getPost();
        post.setHidden(hidePost);  // Ẩn bài viết nếu hidePost là true
        postService.updateReportPost(post);  // Cập nhật bài viết

        report.setStatus(ReportStatus.RESOLVED);
        report.setResolvedAt(LocalDateTime.now());
        report.setDescription("Report resolved at: " + LocalDateTime.now()
                + ". Action taken: " + (hidePost ? "Post hidden" : "Post remains visible"));

        Report savedReport = reportRepository.save(report);
        return reportMapper.toReport2Dto(savedReport);
    }

    @Override
    @Transactional
    public void restorePost(Long postId) {
        // Lấy tất cả các reports có cùng postId và status RESOLVED
        List<Report> reports = reportRepository.findByPostIdAndStatus(postId, ReportStatus.RESOLVED);
        if (reports.isEmpty()) {
            throw new ResourceNotFoundException("No resolved reports found for post ID: " + postId);
        }

        // Lấy post từ report đầu tiên (vì tất cả đều refer đến cùng một post)
        Post post = reports.get(0).getPost();
        if (!post.isHidden()) {
            throw new IllegalStateException("Post is already visible");
        }

        // Cập nhật post
        post.setHidden(false);
        postService.updateReportPost(post);

        // Cập nhật tất cả các reports liên quan
        LocalDateTime now = LocalDateTime.now();
        for (Report report : reports) {
            report.setStatus(ReportStatus.PENDING);
            report.setDescription(report.getDescription() + "\nPost restored at: " + now);
            reportRepository.save(report);
        }
    }


    @Override
    public Report2Dto dismissReport(Long reportId, String reason) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        if (report.getStatus() != ReportStatus.PENDING) {
            throw new IllegalStateException("Cannot dismiss report. Expected status: PENDING, but was: " + report.getStatus());
        }
        report.setStatus(ReportStatus.DISMISSED);
        report.setReason(reason);
        Report savedReport = reportRepository.save(report);
        return reportMapper.toReport2Dto(savedReport);
    }



    private Report findReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
    }

    private void validateReportStatus(Report report, ReportStatus expectedStatus) {
        if (report.getStatus() != expectedStatus) {
            throw new IllegalStateException(
                    "Invalid report status. Expected: " + expectedStatus + ", but was: " + report.getStatus()
            );
        }
    }

}
