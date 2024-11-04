package org.example.library.service;

import jakarta.transaction.Transactional;
import org.example.library.dto.PostDto;
import org.example.library.model.Post;
import org.example.library.dto.PostReportDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

//    PostDto getPostById(Long id);

    List<PostDto> getAllPosts(Long currentUserId);

     List<PostDto> get5Posts();
    List<PostDto> getPostsByUserId(Long userId, String currentUsername);

    public PostDto getPostById(Long PostId);

    PostDto updatePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

    @Transactional
    void deletePost(Long id);

    Post findPostById(Long postId);

    void changePostVisibility(Long id, boolean hidden);

    List<Post> getFilteredPosts(Long currentUserId);

    PostDto getPostByPostId(Long postId);

    void save(Post post);

    //ADMIN
    PostDto findPostByIdadmin(Long id);

    List<PostDto> findAllPosts();

    List<PostDto> findNewPosts(); // Phương thức lấy bài mới

    List<PostDto> findTrendingPosts(); // Phương thức lấy bài xu hướng

    List<PostReportDto> findAllReports(); // Phương thức lấy danh sách báo cáo

    long countTotalPosts();

    List<PostDto> searchPostsByKeyword(String keyword);

    boolean userCanToggleHidden(Long postId, String username);

//    PostDto createPost(PostDto postDto);
 Map<LocalDateTime, Long> countPostByDateRange(LocalDate startDate, LocalDate endDate);

}