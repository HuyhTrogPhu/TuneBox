package org.example.library.service;

import org.example.library.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest; // Thêm import này nếu chưa có

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images, HttpServletRequest request) throws IOException;

    PostDto getPostById(Long id);

    List<PostDto> getAllPosts();

    List<PostDto> getPostsByUserId(Long userId);
}
