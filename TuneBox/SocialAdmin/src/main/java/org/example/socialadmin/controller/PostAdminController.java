package org.example.socialadmin.controller;

import org.example.library.Exception.PostNotFoundException;
import org.example.library.dto.*;
import org.example.library.service.PostService;
import org.example.library.service.ReportService;
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
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/admin/posts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PostAdminController {

    @Autowired
    private PostService postService; // Tiêm PostService

    @Autowired
    private ReportService reportService;

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


    //search new posts
    @GetMapping("/new")
    public ResponseEntity<List<PostDto>> getNewPosts() {
        try {
            List<PostDto> newPosts = postService.findNewPosts();
            return ResponseEntity.ok(newPosts);
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
