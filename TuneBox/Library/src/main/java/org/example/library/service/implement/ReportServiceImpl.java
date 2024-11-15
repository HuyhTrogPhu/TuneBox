package org.example.library.service.implement;

import org.example.library.Exception.ResourceNotFoundException;
import org.example.library.dto.NotificationDTO;
import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDetailDto;
import org.example.library.dto.ReportDto;
import org.example.library.mapper.ReportMapper;
import org.example.library.model.*;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.*;
import org.example.library.service.NotificationService;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationServiceImpl notificationServiceImpl;


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
    public List<Report2Dto> getPendingReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Report> pendingReports = reportRepository.findByStatusAndDateRange(
                ReportStatus.PENDING,
                startDate,
                endDate
        );
        return mapReportsToDto(pendingReports);
    }



    @Override
    @Transactional(readOnly = true)
    public List<Report2Dto> getPendingReportsBySpecificDate(LocalDate specificDate) {
        List<Report> pendingReports = reportRepository.findByStatusAndSpecificDate(
                ReportStatus.PENDING,
                specificDate
        );
        return mapReportsToDto(pendingReports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report2Dto> getAllPendingReports() {
        List<Report> pendingReports = reportRepository.findByStatusAndTypeAndPost(ReportStatus.PENDING, "post", null);
        return mapReportsToDto(pendingReports);
    }



    private List<Report2Dto> mapReportsToDto(List<Report> reports) {
        Map<Long, List<Report>> groupedReports = reports.stream()
                .collect(Collectors.groupingBy(report -> report.getPost().getId()));

        List<Report2Dto> reportDtos = new ArrayList<>();

        for (Map.Entry<Long, List<Report>> entry : groupedReports.entrySet()) {
            List<Report> reportsForPost = entry.getValue();
            Report representativeReport = reportsForPost.get(0);
            Report2Dto dto = reportMapper.toReport2Dto(representativeReport);

            List<ReportDetailDto> details = reportsForPost.stream()
                    .map(report -> new ReportDetailDto(
                            report.getId(),
                            report.getUser().getUserName(),
                            report.getReason(),
                            report.getCreateDate()
                    ))
                    .collect(Collectors.toList());

            dto.setReportDetails(details);
            reportDtos.add(dto);
        }

        return reportDtos;
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

        String message = "Cảm ơn bạn đã góp phần xây dựng cộng đồng lành mạnh. Báo cáo của bạn đã được xử lý.";
        notificationService.createNotificationForUser(report.getUser(), message, "REPORT_RESOLVED");

        return reportMapper.toReport2Dto(savedReport);
    }

    @Override
    @Transactional
    public List<Report2Dto> dismissAllReports(Long postId, String reason) {
        List<Report> reports = reportRepository.findByPostIdAndStatus(postId, ReportStatus.PENDING);

        if (reports.isEmpty()) {
            throw new ResourceNotFoundException("No pending reports found for post: " + postId);
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Dismiss reason cannot be empty");
        }

        LocalDateTime now = LocalDateTime.now();
        List<Report2Dto> dismissedReports = new ArrayList<>();

        for (Report report : reports) {
            report.setStatus(ReportStatus.DISMISSED);
            report.setReason(reason);
            report.setResolvedAt(now);
            report.setDescription("Report dismissed at: " + now + ". Reason: " + reason);

            Report savedReport = reportRepository.save(report);
            dismissedReports.add(reportMapper.toReport2Dto(savedReport));

            String message = "Cảm ơn bạn đã gửi báo cáo. Chúng tôi đã xem xét và quyết định bỏ qua báo cáo này.";
            notificationServiceImpl.createNotificationForUser(report.getUser(), message, "REPORT_DISMISSED");
        }

        return dismissedReports;
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





