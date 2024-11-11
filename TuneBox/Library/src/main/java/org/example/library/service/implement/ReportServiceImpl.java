package org.example.library.service.implement;

import org.example.library.Exception.ResourceNotFoundException;
import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDto;
import org.example.library.mapper.ReportMapper;
import org.example.library.model.*;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.*;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrackRepository trackRepository; // Để lấy track khi cần

    @Autowired
    private AlbumsRepository albumRepository; // Để lấy album khi cần

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private PostService postService;

    @Override
    public boolean checkReportExists(Long userId, Long postId, Long trackId, Long albumId, String type) {
        // Logic để kiểm tra báo cáo tồn tại
        if (type.equals("post")) {
            return reportRepository.existsByUserIdAndPostId(userId, postId);
        } else if (type.equals("track")) {
            return reportRepository.existsByUserIdAndTrackId(userId, trackId);
        } else if (type.equals("album")) {
            return reportRepository.existsByUserIdAndAlbumId(userId, albumId);
        }
        return false;
    }

        @Override
    public ReportDto createReport(ReportDto reportDto) {
        boolean reportExists = reportRepository.existsByUserIdAndType(
                reportDto.getUserId(),
                reportDto.getPostId(),
                reportDto.getTrackId(),
                reportDto.getAlbumId(),
                reportDto.getType()
        );

        if (reportExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bạn đã báo cáo nội dung này rồi.");
        }

        Report report = reportMapper.toEntity(reportDto);
        report.setCreateDate(LocalDate.now());
        report.setStatus(ReportStatus.PENDING);

        User user = userRepository.findById(reportDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        report.setUser(user);

        switch (reportDto.getType()) {
            case "post":
                Post post = postRepository.findById(reportDto.getPostId())
                        .orElseThrow(() -> new RuntimeException("Post không tồn tại"));
                report.setPost(post);
                break;
            case "track":
                Track track = trackRepository.findById(reportDto.getTrackId())
                        .orElseThrow(() -> new RuntimeException("Track không tồn tại"));
                report.setTrack(track);
                break;
            case "album":
                Albums album = albumRepository.findById(reportDto.getAlbumId())
                        .orElseThrow(() -> new RuntimeException("Album không tồn tại"));
                report.setAlbum(album);
                break;
            default:
                throw new IllegalArgumentException("Loại báo cáo không hợp lệ");
        }

        Report savedReport = reportRepository.save(report);
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


    @Override
    public ReportDto updateApprove(Long id) {
        Report report = reportRepository.findById(id).get();
        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);
        return reportMapper.toDto(report);
    }
    @Override
    public ReportDto updateDenied(Long id) {
        Report report = reportRepository.findById(id).get();
        report.setStatus(ReportStatus.DISMISSED);
        reportRepository.save(report);
        return reportMapper.toDto(report);
    }
}
