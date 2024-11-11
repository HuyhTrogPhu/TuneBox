package org.example.socialadmin.controller;

import org.example.library.dto.*;
import org.example.library.service.PostServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/posts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StatisticalPostController {
    @Autowired
    private PostServiceAdmin postServiceAdmin;

    @GetMapping("/trending")
    public List<PostAdminDto> getTopPostsByInteractions() {
        return postServiceAdmin.getTopPostsByInteractions();
    }

    @GetMapping("/with-images")
    public List<PostAdminDto> getPostsWithImages() {
        return postServiceAdmin.findPostsWithImages();
    }

    @GetMapping("/without-images")
    public List<PostAdminDto> getPostsWithoutImages() {
        return postServiceAdmin.findPostsWithoutImages();
    }

    @GetMapping("/latest")
    public List<PostAdminDto> getLatestPosts() {
        return postServiceAdmin.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/statistics")
    public PostStatisticsDto getPostStatistics() {
        return postServiceAdmin.getPostStatistics();
    }

    @GetMapping("/engagement")
    public List<PostEngagementDto> getPostEngagementStats() {
        return postServiceAdmin.getPostEngagementStats();
    }

    @GetMapping("/daily")
    public List<DailyPostStatsDto> getDailyPostStats() {
        return postServiceAdmin.getDailyPostStats();
    }
}
