
package org.example.library.service;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

//    PostDto getPostById(Long id);

    List<PostDto> getAllPosts(Long currentUserId);

    List<PostDto> getPostsByUserId(Long userId);

    PostDto updatePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

    @Transactional
    void deletePost(Long id);

    Post findPostById(Long postId);

    void changePostVisibility(Long id, boolean hidden);

    List<Post> getFilteredPosts(Long currentUserId);
}