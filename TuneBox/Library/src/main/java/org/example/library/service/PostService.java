package org.example.library.service;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

//    PostDto getPostById(Long id);

    List<PostDto> getAllPosts(Long currentUserId);

    List<PostDto> getPostsByUserId(Long userId);

    PostDto updatePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

    void deletePost(Long id);

//    Post findPostById(Long postId);

    void changePostVisibility(Long id, boolean hidden);

    List<Post> getFilteredPosts(Long currentUserId);

    //ADMIN
    PostDto findPostById(Long id);

    List<PostDto> findAllPosts();

    List<PostDto> findNewPosts(); // Phương thức lấy bài mới

//    List<PostDto> findTrendingPosts(); // Phương thức lấy bài xu hướng

//    List<PostReportDto> findAllReports(); // Phương thức lấy danh sách báo cáo

    long countTotalPosts();


}
