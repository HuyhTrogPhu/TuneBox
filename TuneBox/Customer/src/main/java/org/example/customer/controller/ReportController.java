package org.example.customer.controller;

import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDto;
import org.example.library.dto.ReportDto3;
import org.example.library.model.Post;
import org.example.library.model_enum.ReportStatus;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000",
        allowCredentials = "true",
        allowedHeaders = {"Content-Type", "Accept"},
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<ReportDto3> createReport(
            @RequestBody ReportDto3 reportDto,
            @CookieValue(value = "userId", defaultValue = "0") Long currentUserId
    ) {
        // Kiểm tra nếu userId từ cookie không hợp lệ
        if (currentUserId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        System.out.println("Payload received: " + reportDto);

        System.out.println("cookies: " + currentUserId);

        // Kiểm tra xem bài viết có tồn tại hay không
        if (reportDto.getPost() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Post post = postService.findThisPostById(reportDto.getPost().getReportedId());
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
        ReportDto3 createdReport = reportService.createReport(reportDto);
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

    @GetMapping("/pending")
    public ResponseEntity<List<Report2Dto>> getPendingReports() {
        try {
            List<Report2Dto> pendingReports = reportService.getPendingReports(); // Gọi phương thức không phân trang
            return ResponseEntity.ok(pendingReports);
        } catch (Exception e) {
            System.out.println("Error fetching pending reports: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{reportId}/resolve")
    public ResponseEntity<Report2Dto> resolveReport(@PathVariable Long reportId, @RequestParam boolean hidePost) {
        try {
            Report2Dto resolvedReport = reportService.resolveReport(reportId, hidePost);
            return ResponseEntity.ok(resolvedReport);
        } catch (RuntimeException e) {
            // Thêm logging
            System.out.println("Error resolving report: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/{reportId}/restore")
    public ResponseEntity<Void> restorePost(@PathVariable Long reportId) {
        try {
            Report2Dto reportDto = reportService.getReport2ById(reportId);

            if (reportDto != null && reportDto.getPost() != null) {
                reportService.restorePost(reportDto.getPost().getPostId());
                return ResponseEntity.ok().build();
            } else {
                System.out.println("Report or Post not found for reportId: " + reportId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (RuntimeException e) {
            System.out.println("Error restore report: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{reportId}/dismiss")
    public ResponseEntity<Report2Dto> dismissReport(
            @PathVariable Long reportId,
            @RequestParam(required = false) String reason
    ) {
        try {
            Report2Dto dismissedReport = reportService.dismissReport(reportId, reason);
            return ResponseEntity.ok(dismissedReport);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Xử lý báo cáo
//    @PutMapping("/{reportId}/process")
//    public ResponseEntity<ReportDto> processReport(
//            @PathVariable Long reportId,
//            @RequestBody ReportProcessRequest request
//    ) {
//        try {
//            ReportDto processedReport = reportService.processReport(reportId, request);
//            return ResponseEntity.ok(processedReport);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
}