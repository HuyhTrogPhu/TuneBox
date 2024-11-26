package org.example.socialadmin.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.library.Exception.PostNotFoundException;
import org.example.library.Exception.ResourceNotFoundException;
import org.example.library.dto.*;
import org.example.library.mapper.PostMapper;
import org.example.library.model.Post;
import org.example.library.model.Report;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.PostRepository;
import org.example.library.repository.ReportRepository;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/posts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PostAdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PostRepository postRepository;

    //search all bai viet
    @GetMapping
    public ResponseEntity<Page<PostDto>> getAllPosts(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) LocalDate specificDate,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<PostDto> posts;
            if (specificDate != null) {
                posts = postService.findPostsBySpecificDate(specificDate, pageable);
            } else if (startDate != null && endDate != null) {
                posts = postService.findPostsByDateRange(startDate, endDate, pageable);
            } else {
                posts = postService.findAllPosts(pageable);
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<Report2Dto>> getPendingReports(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) LocalDate specificDate,
            @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<Report2Dto> pendingReports;
            if (specificDate != null) {
                pendingReports = reportService.getPendingReportsBySpecificDate(specificDate, pageable);
            } else if (startDate != null && endDate != null) {
                pendingReports = reportService.getPendingReportsByDateRange(startDate, endDate, pageable);
            } else {
                pendingReports = reportService.getAllPendingReports(pageable);
            }
            return ResponseEntity.ok(pendingReports);
        } catch (Exception e) {
            System.out.println("Error fetching pending reports: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{postId}/restore")
    public ResponseEntity<?> restorePost(@PathVariable Long postId) {
        try {
            Post post = postService.restorePost(postId);
            return ResponseEntity.ok(PostMapper.toDto(post));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error restoring post", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{reportId}/resolve")
    public ResponseEntity<?> resolvePost(@PathVariable Long reportId, @RequestParam(required = false) String reason) {
        try {
            log.info("Resolving post with reportId: {} and reason: {}", reportId, reason);
            Post post = postService.resolvePost(reportId, reason);
            return ResponseEntity.ok(PostMapper.toDto(post));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error resolving post with reportId: " + reportId, e);
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }


    @PutMapping("/{postId}/dismiss")
    public ResponseEntity<List<Report2Dto>> dismissAllReports(
            @PathVariable Long postId,
            @RequestParam String reason) {
        if (postId == null || postId <= 0) {
            log.error("Invalid postId: {}", postId);
            return ResponseEntity.badRequest().body(null);
        }

        if (reason == null || reason.trim().isEmpty()) {
            log.error("Dismiss reason cannot be empty");
            return ResponseEntity.badRequest().body(null);
        }
        try {
            List<Report2Dto> dismissedReports = postService.dismissAllReports(postId, reason);
            return ResponseEntity.ok(dismissedReports);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Error dismissing reports for post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{postId}/reports")
    public ResponseEntity<List<Report2Dto>> getPostReports(@PathVariable Long postId) {
        try {
            log.info("Fetching reports for post: {}", postId);
            List<Report2Dto> reports = postService.getPostReports(postId);
            return ResponseEntity.ok(reports);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error fetching reports for post: " + postId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/admin-hidden-resolved-posts")
    public ResponseEntity<List<PostDto>> getAdminHiddenAndResolvedPosts() {
        try {
            List<PostDto> posts = reportService.getAdminHiddenAndResolvedPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/new")
    public ResponseEntity<CountNewPostInDayDto> getNewPosts() {
        try {
            CountNewPostInDayDto summary = postService.findNewPosts();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalPosts() {
        try {
            long totalPosts = postService.countTotalPosts(); // Gọi phương thức để lấy tổng số bài viết
            return ResponseEntity.ok(totalPosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //search theo id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        try {
            PostDto post = postService.findPostByIdadmin(id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //update post
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostDto postDto,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws IOException {
        try {
            postDto.setId(id);
            Long userId = postDto.getUserId();
            PostDto updatedPost = postService.updatePost(postDto, images, userId);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //xoa bai viet
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (PostNotFoundException e) {  // Ngoại lệ cụ thể khi không tìm thấy bài viết
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting post");
        }
    }

    //search-information
    @GetMapping("/search-info/{postId}")
    public ResponseEntity<UserInfoDto> getSearchInfo(@PathVariable Long postId) {
        try {
            UserInfoDto userInfoDto = postService.getSearchInfo(postId);
            return ResponseEntity.ok(userInfoDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
