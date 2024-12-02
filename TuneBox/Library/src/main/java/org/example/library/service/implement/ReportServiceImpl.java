package org.example.library.service.implement;

import org.example.library.Exception.ResourceNotFoundException;
import org.example.library.dto.*;
import org.example.library.mapper.PostMapper;
import org.example.library.mapper.ReportMapper;
import org.example.library.model.*;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.*;
import org.example.library.service.NotificationService;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);


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


     User reportingUser = userRepository.findById(reportDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        report.setUser(reportingUser);
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
            case "user":
                User reportedUser = userRepository.findById(reportDto.getReportedId()).get();
                report.setReportedUser(reportedUser);
                break;
            default:
                throw new IllegalArgumentException("Loại báo cáo không hợp lệ");
        }
        // Lưu report
        Report savedReport = reportRepository.save(report);

        // Trả về DTO
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
    public Report2Dto dismissReport(Long reportId, String reason) {
        return null;
    }


    @Override
    public List<ReportDto> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Report2Dto resolveReport(Long reportId, boolean hidePost) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Report2Dto> getPendingReportsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Report> pendingReports = reportRepository.findByStatusAndDateRange(
                ReportStatus.PENDING,
                startDate,
                endDate,
                pageable
        );
        List<Report2Dto> reportDtos = mapReportsToDto(pendingReports.getContent());
        return new PageImpl<>(reportDtos, pageable, pendingReports.getTotalElements());
    }



    @Override
    @Transactional(readOnly = true)
    public Page<Report2Dto> getPendingReportsBySpecificDate(LocalDate specificDate, Pageable pageable) {
        Page<Report> pendingReports = reportRepository.findByStatusAndSpecificDate(
                ReportStatus.PENDING,
                specificDate,
                pageable
        );
        List<Report2Dto> reportDtos = mapReportsToDto(pendingReports.getContent());
        return new PageImpl<>(reportDtos, pageable, pendingReports.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Report2Dto> getAllPendingReports(Pageable pageable) {
        Page<Report> pendingReports = reportRepository.findByStatusAndType(ReportStatus.PENDING, "post", pageable);
        List<Report2Dto> reportDtos = mapReportsToDto(pendingReports.getContent());
        return new PageImpl<>(reportDtos, pageable, pendingReports.getTotalElements());
    }



    private Report2Dto mapReportToDto(Report report) {
        Report2Dto dto = reportMapper.toReport2Dto(report);

        dto.setReason(report.getReason() != null ? report.getReason() : "");
        dto.setStatus(report.getStatus());
        dto.setCreateDate(report.getCreateDate() != null ? report.getCreateDate() : LocalDate.now());
        dto.setDescription(report.getDescription() != null ? report.getDescription() : "");

        return dto;
    }



    private List<Report2Dto> mapReportsToDto(List<Report> reports) {
        // Nhóm các báo cáo theo bài viết (post)
        Map<Long, List<Report>> groupedReports = reports.stream()
                .collect(Collectors.groupingBy(report -> report.getPost().getId()));

        List<Report2Dto> reportDtos = new ArrayList<>();

        // Lặp qua các nhóm báo cáo cho mỗi bài viết
        for (Map.Entry<Long, List<Report>> entry : groupedReports.entrySet()) {
            List<Report> reportsForPost = entry.getValue();

            // Lấy báo cáo đại diện cho bài viết
            Report representativeReport = reportsForPost.get(0);

            // Tạo DTO cho báo cáo
            Report2Dto dto = reportMapper.toReport2Dto(representativeReport);

            // Tạo danh sách chi tiết báo cáo từ các báo cáo liên quan đến bài viết này
            List<ReportDetailDto> details = reportsForPost.stream()
                    .map(report -> new ReportDetailDto(
                            report.getId(),
                            report.getUser().getUserName(),
                            report.getReason(),
                            report.getCreateDate()
                    ))
                    .collect(Collectors.toList());

            // Thiết lập danh sách chi tiết báo cáo
            dto.setReportDetails(details);

            // Cập nhật số lượng báo cáo (số lượng báo cáo cho bài viết này)
            dto.setReportCount(reportsForPost.size()); // Đếm số lượng báo cáo cho bài viết này

            // Thêm DTO vào danh sách
            reportDtos.add(dto);
        }

        return reportDtos;
    }



    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getAdminHiddenAndResolvedPosts() {
        List<Post> posts = reportRepository.findAdminHiddenAndResolvedPosts(ReportStatus.RESOLVED);
        return posts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
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
        if (!report.getStatus().equals("RESOLVED")) {
            report.setStatus(ReportStatus.RESOLVED);
            if(report.getType().equals("user")){
                User reportedUser = userRepository.findById(report.getReportedUser().getId()).get();
                reportedUser.setReportCount(reportedUser.getReportCount() + 1);
            }
        }
        reportRepository.save(report);
        return reportMapper.toDto(report);
    }

    @Override
    public List<ReportDto> updateApproveTrackId(Long id) {
        List<Report> listTrackReport = reportRepository.findAllByTrackId(id);
        for (Report rp : listTrackReport) {
            updateApprove(rp.getId());
        }
        // Chuyển đổi từng phần tử
        return listTrackReport.stream()
                .map(reportMapper::toDto) // Chuyển từng phần tử từ Report sang ReportDto
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDto> updateApproveAlbumId(Long id) {
        List<Report> listTrackReport = reportRepository.findAllByAlbumId(id);
        for (Report rp : listTrackReport) {
            updateApprove(rp.getId());
        }
        // Chuyển đổi từng phần tử
        return listTrackReport.stream()
                .map(reportMapper::toDto) // Chuyển từng phần tử từ Report sang ReportDto
                .collect(Collectors.toList());
    }



    @Override
    public ReportDto updateDenied(Long id) {
        Report report = reportRepository.findById(id).get();
        if (!report.getStatus().equals("DISMISSED")) {
            report.setStatus(ReportStatus.DISMISSED);
            if(report.getType().equals("user")){
                User reportedUser = userRepository.findById(report.getReportedUser().getId()).get();
                reportedUser.setReportCount(reportedUser.getReportCount() - 1);
            }
        }
        reportRepository.save(report);
        return reportMapper.toDto(report);
    }

    @Override
    public List<ReportDto> updateDeniedTrackId(Long id) {
        List<Report> listTrackReport = reportRepository.findAllByTrackId(id);
        for (Report rp : listTrackReport) {
            updateDenied(rp.getId());
        }
        // Chuyển đổi từng phần tử
        return listTrackReport.stream()
                .map(reportMapper::toDto) // Chuyển từng phần tử từ Report sang ReportDto
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDto> updateDeniedAlbumId(Long id) {
        List<Report> listTrackReport = reportRepository.findAllByTrackId(id);
        for (Report rp : listTrackReport) {
            updateDenied(rp.getId());
        }
        // Chuyển đổi từng phần tử
        return listTrackReport.stream()
                .map(reportMapper::toDto) // Chuyển từng phần tử từ Report sang ReportDto
                .collect(Collectors.toList());
    }



    @Override
    public Page<ReportDtoSocialAdmin> findAllReportsWithTracks(Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllReportsWithTracks(pageable);  // Truyền pageable vào repository
        return reportPage.map(rp -> new ReportDtoSocialAdmin(
                rp.getId(),
                rp.getCreateDate(),
                rp.getPost() != null ? rp.getPost().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getName() : null,
                rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                rp.getUser() != null ? rp.getUser().getId() : null,
                rp.getUser() != null ? rp.getUser().getUserName() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                rp.getStatus(),
                rp.getResolvedAt(),
                rp.getDescription(),
                rp.getType(),
                rp.getReason()
        ));
    }

    @Override
    public Page<ReportDtoSocialAdmin> findAllReportsWithUser(Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllReportsWithUser(pageable);  // Truyền pageable vào repository
        return reportPage.map(rp -> new ReportDtoSocialAdmin(
                rp.getId(),
                rp.getCreateDate(),
                rp.getPost() != null ? rp.getPost().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getName() : null,
                rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                rp.getUser() != null ? rp.getUser().getId() : null,
                rp.getUser() != null ? rp.getUser().getUserName() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                rp.getStatus(),
                rp.getResolvedAt(),
                rp.getDescription(),
                rp.getType(),
                rp.getReason()
        ));
    }


    @Override
    public Page<ReportDtoSocialAdmin> findAllReportsWithAlbum(Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllReportsWithAlbum(pageable);  // Truyền pageable vào repository
        return reportPage.map(rp -> new ReportDtoSocialAdmin(
                rp.getId(),
                rp.getCreateDate(),
                rp.getPost() != null ? rp.getPost().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getName() : null,
                rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                rp.getUser() != null ? rp.getUser().getId() : null,
                rp.getUser() != null ? rp.getUser().getUserName() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                rp.getStatus(),
                rp.getResolvedAt(),
                rp.getDescription(),
                rp.getType(),
                rp.getReason()
        ));
    }

    @Override
    public Page<ReportDtoSocialAdmin> findAllReportsWithPost(Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllReportsWithPost(pageable);  // Truyền pageable vào repository
        return reportPage.map(rp -> new ReportDtoSocialAdmin(
                rp.getId(),
                rp.getCreateDate(),
                rp.getPost() != null ? rp.getPost().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getName() : null,
                rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                rp.getUser() != null ? rp.getUser().getId() : null,
                rp.getUser() != null ? rp.getUser().getUserName() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                rp.getStatus(),
                rp.getResolvedAt(),
                rp.getDescription(),
                rp.getType(),
                rp.getReason()
        ));
    }

    @Override
    public ReportDtoSocialAdmin findById(Long id){
        Report rp = reportRepository.findById(id).get();
        return new ReportDtoSocialAdmin(
                rp.getId(),
                rp.getCreateDate(),
                rp.getPost() != null ? rp.getPost().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getId() : null,
                rp.getTrack() != null ? rp.getTrack().getName() : null,
                rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                rp.getUser() != null ? rp.getUser().getId() : null,
                rp.getUser() != null ? rp.getUser().getUserName() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                rp.getStatus(),
                rp.getResolvedAt(),
                rp.getDescription(),
                rp.getType(),
                rp.getReason()
        );
}

    @Override
    public List<ReportDtoSocialAdmin> findByTrackId(Long id){
        List<Report> report = reportRepository.findAllByTrackId(id);
        List<ReportDtoSocialAdmin> reportDtos = report.stream()
                .map(rp -> new ReportDtoSocialAdmin(
                        rp.getId(),
                        rp.getCreateDate(),
                        rp.getPost() != null ? rp.getPost().getId() : null,
                        rp.getTrack() != null ? rp.getTrack().getId() : null,
                        rp.getTrack() != null ? rp.getTrack().getName() : null,
                        rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                        rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                        rp.getUser() != null ? rp.getUser().getId() : null,
                        rp.getUser() != null ? rp.getUser().getUserName() : null,
                        rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                        rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                        rp.getStatus(),
                        rp.getResolvedAt(),
                        rp.getDescription(),
                        rp.getType(),
                        rp.getReason()
                ))
                .collect(Collectors.toList());
        return reportDtos;
    }

    @Override
    public List<ReportDtoSocialAdmin> findByAlbumId(Long id){
        List<Report> report = reportRepository.findAllByAlbumId(id);
        List<ReportDtoSocialAdmin> reportDtos = report.stream()
                .map(rp -> new ReportDtoSocialAdmin(
                        rp.getId(),
                        rp.getCreateDate(),
                        rp.getPost() != null ? rp.getPost().getId() : null,
                        rp.getTrack() != null ? rp.getTrack().getId() : null,
                        rp.getTrack() != null ? rp.getTrack().getName() : null,
                        rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                        rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                        rp.getUser() != null ? rp.getUser().getId() : null,
                        rp.getUser() != null ? rp.getUser().getUserName() : null,
                        rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                        rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                        rp.getStatus(),
                        rp.getResolvedAt(),
                        rp.getDescription(),
                        rp.getType(),
                        rp.getReason()
                ))
                .collect(Collectors.toList());
        return reportDtos;
    }

    @Override
    public List<ReportDtoSocialAdmin> findByReportedId(Long id){
        List<Report> report = reportRepository.findAllByReportedUserId(id);
        List<ReportDtoSocialAdmin> reportDtos = report.stream()
                .map(rp -> new ReportDtoSocialAdmin(
                        rp.getId(),
                        rp.getCreateDate(),
                        rp.getPost() != null ? rp.getPost().getId() : null,
                        rp.getTrack() != null ? rp.getTrack().getId() : null,
                        rp.getTrack() != null ? rp.getTrack().getName() : null,
                        rp.getAlbum() != null ? rp.getAlbum().getId() : null,
                        rp.getAlbum() != null ? rp.getAlbum().getTitle() : null,
                        rp.getUser() != null ? rp.getUser().getId() : null,
                        rp.getUser() != null ? rp.getUser().getUserName() : null,
                        rp.getReportedUser() != null ? rp.getReportedUser().getId() : null,
                        rp.getReportedUser() != null ? rp.getReportedUser().getUserName() : null,
                        rp.getStatus(),
                        rp.getResolvedAt(),
                        rp.getDescription(),
                        rp.getType(),
                        rp.getReason()
                ))
                .collect(Collectors.toList());
        return reportDtos;
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
}





