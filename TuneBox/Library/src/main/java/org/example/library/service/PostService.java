package org.example.library.service;

import jakarta.transaction.Transactional;
import org.example.library.dto.PostDto;
import org.example.library.dto.ReportDto;
import org.example.library.model.Post;
//import org.example.library.dto.PostReportDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

//    PostDto getPostById(Long id);

    List<PostDto> getAllPosts(Long currentUserId);

    List<PostDto> getPostsByUserId(Long userId, String currentUsername);

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


    List<PostDto> findAllPosts();

    List<PostDto> findNewPosts(); // Phương thức lấy bài mới

    long countTotalPosts();

    List<PostDto> searchPostsByKeyword(String keyword);

    boolean userCanToggleHidden(Long postId, String username);

//    PostDto createPost(PostDto postDto);

    List<ReportDto> getReportedPosts(); // Lấy danh sách các bài viết bị báo cáo

//    List<PostReportDto> findAllReports(); // Phương thức lấy danh sách báo cáo

}