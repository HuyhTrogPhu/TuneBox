package org.example.library.service;

import org.example.library.dto.PostDto;
import org.example.library.dto.ReportDto;
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

    void updateReportPost(Post post);


    Post findThisPostById(Long postId);

    void changePostVisibility(Long id, boolean hidden);

    List<Post> getFilteredPosts(Long currentUserId);

    //ADMIN
    PostDto findPostByIdadmin(Long id);

    PostDto findPostById(Long id);

    PostDto getPostByPostId(Long postId);


    List<PostDto> findAllPosts();

    List<PostDto> findNewPosts(); // Phương thức lấy bài mới

    long countTotalPosts();

    List<ReportDto> getReportedPosts(); // Lấy danh sách các bài viết bị báo cáo

}