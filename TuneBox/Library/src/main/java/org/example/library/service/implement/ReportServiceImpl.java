package org.example.library.service.implement;

import org.example.library.dto.ReportDto;
import org.example.library.mapper.ReportMapper;
import org.example.library.model.*;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.*;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class    ReportServiceImpl implements ReportService {

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
    public List<ReportDto> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }
}
