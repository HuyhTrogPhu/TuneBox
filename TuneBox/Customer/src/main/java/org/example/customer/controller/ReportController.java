package org.example.customer.controller;

import org.example.library.dto.ReportDto;
import org.example.library.model.Post;
import org.example.library.repository.ReportRepository;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private PostService postService;
    @Autowired
    ReportRepository reportRepository;


    @PostMapping
    public ResponseEntity<ReportDto> createReport(
            @RequestBody ReportDto reportDto,
            @CookieValue(value = "userId", defaultValue = "0") Long currentUserId // Lấy giá trị currentUserId từ cookie
    ) {
        // Kiểm tra nếu userId từ cookie không hợp lệ
        if (currentUserId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Trả về 401 nếu không có userId hợp lệ
        }
        // Kiểm tra xem bài viết có tồn tại hay không
        Post post = postService.findPostById(reportDto.getPostId());
        if (post == null) {
            return ResponseEntity.badRequest().body(null);
        }
        // Kiểm tra lý do báo cáo
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        // Set userId hiện tại cho reportDto
        reportDto.setUserId(currentUserId);
        // Tiến hành tạo báo cáo
        ReportDto createdReport = reportService.createReport(reportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable Long id) {
        ReportDto reportDto = reportService.getReportById(id);
        return ResponseEntity.ok(reportDto);
    }

    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports() {
        List<ReportDto> reportDtos = reportService.getAllReports();
        return ResponseEntity.ok(reportDtos);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkReport(
            @RequestParam Long userId,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long trackId,
            @RequestParam(required = false) Long albumId,
            @RequestParam String type) {

        System.out.println("Received userId: " + userId + ", postId: " + postId + ", trackId: " + trackId + ", albumId: " + albumId + ", type: " + type);

        boolean reportExists = reportRepository.existsByUserIdAndType(userId, postId, trackId, albumId, type);
        return ResponseEntity.ok(reportExists);
    }


}
