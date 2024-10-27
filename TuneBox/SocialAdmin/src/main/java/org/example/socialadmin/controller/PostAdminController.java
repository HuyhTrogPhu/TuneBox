//package org.example.socialadmin.controller;
//
//import org.example.library.Exception.PostNotFoundException;
//import org.example.library.dto.PostDto;
//import org.example.library.dto.PostReportDto;
//import org.example.library.service.PostService; // Giả định bạn đã có service cho Post
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/admin/posts")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
//public class PostAdminController {
//
//    @Autowired
//    private PostService postService; // Tiêm PostService
//
//    //search all bai viet
//    @GetMapping
//    public ResponseEntity<List<PostDto>> getAllPosts() {
//        try {
//            List<PostDto> posts = postService.findAllPosts();
//            return ResponseEntity.ok(posts);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/new")
//    public ResponseEntity<List<PostDto>> getNewPosts() {
//        try {
//            List<PostDto> newPosts = postService.findNewPosts(); // Giả định có phương thức lấy bài mới
//            return ResponseEntity.ok(newPosts);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/trending")
//    public ResponseEntity<List<PostDto>> getTrendingPosts() {
//        try {
//            List<PostDto> trendingPosts = postService.findTrendingPosts(); // Giả định có phương thức lấy bài xu hướng
//            return ResponseEntity.ok(trendingPosts);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//
////    @GetMapping("/reports")
////    public ResponseEntity<List<PostReportDto>> getPostReports() {
////        try {
////            // Lấy danh sách báo cáo
////            List<PostReportDto> reports = postService.findAllReports();
////            SensitiveContentService sensitiveContentService = new SensitiveContentService();
////
////            for (PostReportDto report : reports) {
////                if (sensitiveContentService.containsSensitiveContent(report.getContent())) {
////                    report.setSensitive(true); // Đánh dấu bài đăng có nội dung nhạy cảm
////                } else {
////                    report.setSensitive(false); // Bài đăng không có nội dung nhạy cảm
////                }
////            }
////
////            // Trả về danh sách báo cáo đã được đánh dấu
////            return ResponseEntity.ok(reports);
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
////        }
////    }
//
//
//    @GetMapping("/total")
//    public ResponseEntity<Long> getTotalPosts() {
//        try {
//            long totalPosts = postService.countTotalPosts(); // Gọi phương thức để lấy tổng số bài viết
//            return ResponseEntity.ok(totalPosts);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//
//
//    //search theo id
//    @GetMapping("/{id}")
//    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
//        try {
//            PostDto post = postService.findPostByIdadmin(id);
//            return ResponseEntity.ok(post);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
//
//    //update post
//    @PutMapping("/{id}")
//    public ResponseEntity<PostDto> updatePost(
//            @PathVariable Long id,
//            @RequestBody PostDto postDto,
//            @RequestParam(value = "images", required = false) MultipartFile[] images) throws IOException {
//        try {
//            postDto.setId(id);
//            Long userId = postDto.getUserId();
//            PostDto updatedPost = postService.updatePost(postDto, images, userId);
//            return ResponseEntity.ok(updatedPost);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    //xoa bai viet
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deletePost(@PathVariable Long id) {
//        try {
//            postService.deletePost(id);
//            return ResponseEntity.noContent().build();
//        } catch (PostNotFoundException e) {  // Ngoại lệ cụ thể khi không tìm thấy bài viết
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found with id: " + id);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting post");
//        }
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<PostDto>> searchPostsByKeyword(@RequestParam("keyword") String keyword) {
//        try {
//            List<PostDto> posts = postService.searchPostsByKeyword(keyword);
//            return ResponseEntity.ok(posts);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//
//}