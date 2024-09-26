package org.example.library.service;

import org.example.library.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images); // Thay đổi từ MultipartFile sang MultipartFile[] để hỗ trợ nhiều ảnh

    PostDto updatePost(Long id, PostDto postDto, List<MultipartFile> images);

    PostDto getPostById(long id);

    List<PostDto> getAllPosts();

    void deletePost(long id);
}
