package org.example.customer.controller;

import org.example.customer.config.CustomerDetail;
import org.example.customer.config.JwtUtil;
import org.example.library.dto.ReportDto;
import org.example.library.model.Post;
import org.example.library.model.User;
import org.example.library.repository.ReportRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ReportDto> createReport(
            @RequestBody ReportDto reportDto,
            @RequestHeader(value = "Authorization", required = false) String token // Lấy JWT token từ header
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Trả về 401 nếu không có token hoặc token không hợp lệ
        }

        String jwt = token.substring(7); // Loại bỏ phần "Bearer "
        String username = jwtUtil.extractUsername(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(jwt, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Trả về 401 nếu JWT không hợp lệ
        }

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra các điều kiện khác và tạo báo cáo
        if (reportDto.getReason() == null || reportDto.getReason().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        reportDto.setUserId(user.getId());
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

    public ResponseEntity<Map<String, Boolean>> checkReportExists(
            @RequestParam Long userId,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long trackId,
            @RequestParam(required = false) Long albumId,
            @RequestParam String type) {
        boolean exists = reportService.checkReportExists(userId, postId, trackId, albumId, type);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }


    }
