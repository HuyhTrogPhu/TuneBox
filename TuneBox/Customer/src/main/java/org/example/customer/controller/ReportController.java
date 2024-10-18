package org.example.customer.controller;

import org.example.library.dto.ReportDto;
import org.example.library.model.Post;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<ReportDto> createReport(@RequestBody ReportDto reportDto) {
        // Kiểm tra xem bài viết có tồn tại hay không
        Post post = postService.findPostById(reportDto.getPostId());
        if (post == null) {
            // Trả về ResponseEntity với một ReportDto rỗng hoặc thông điệp lỗi thích hợp
            return ResponseEntity.badRequest().body(null); // Hoặc bạn có thể trả về một ReportDto mới với thông tin chi tiết
        }

        // Kiểm tra lý do báo cáo
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            // Trả về ResponseEntity với một ReportDto rỗng hoặc thông điệp lỗi thích hợp
            return ResponseEntity.badRequest().body(null); // Hoặc bạn có thể trả về một ReportDto mới với thông tin chi tiết
        }

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
}
