package org.example.library.service;

import org.example.library.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDto savePost(PostDto postDto, MultipartFile[] images) throws IOException;
    PostDto getPostById(Long id);

    List<PostDto> getAllPosts();
}
