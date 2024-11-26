package org.example.library.service;

import jakarta.transaction.Transactional;
import org.example.library.dto.*;
import org.example.library.model.Post;
//import org.example.library.dto.PostReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;


    List<PostDto> getAllPosts(Long currentUserId);

     List<PostDto> get5Posts();

    List<PostDto> getPostsByUserId(Long userId, String currentUsername);

//    public PostDto getPostById(Long PostId);

    PostDto updatePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

    @Transactional
    void deletePost(Long id);

    void updateReportPost(Post post);


    Post findThisPostById(Long postId);

    void changePostVisibility(Long id, boolean hidden);

    List<Post> getFilteredPosts(Long currentUserId);

    PostDto getPostByPostId(Long postId);

    void save(Post post);

    //ADMIN
    PostDto findPostByIdadmin(Long id);

    Post findPostById(Long id);

//    PostDto getPostByPostId(Long postId);



    CountNewPostInDayDto findNewPosts(); // Phương thức trả về số lượng và danh sách bài viết

    List<PostDto> findTrendingPosts();

    long countTotalPosts();

    List<PostDto> searchPostsByKeyword(String keyword);

    boolean userCanToggleHidden(Long postId, String username);
    List<PostDto> findAllPostsUser();

//    PostDto createPost(PostDto postDto);
 Map<LocalDateTime, Long> countPostByDateRange(LocalDate startDate, LocalDate endDate);

//    List<PostReportDto> findAllReports(); // Phương thức lấy danh sách báo cáo

    UserInfoDto getSearchInfo(Long postId);

    Page<PostDto> findPostsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<PostDto> findAllPosts(Pageable pageable);
    Page<PostDto> findPostsBySpecificDate(LocalDate specificDate, Pageable pageable);

    // for share post to chat
    PostDto getPostById(Long id);

    Post resolvePost(Long reportId, String reason);

    Post restorePost(Long postId);

    List<Report2Dto> dismissAllReports(Long postId, String reason);


    List<Report2Dto> getPostReports(Long postId);
}