package org.example.library.service;

import org.example.library.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile image);

    PostDto getPostById(long id);

    List<PostDto> getAllPosts();

    PostDto updatePost(Long id,PostDto postDto,MultipartFile image);

    void deletePost(long id);
}
