
package org.example.library.service;

import org.example.library.dto.PostDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface PostService {

    PostDto savePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

//    PostDto getPostById(Long id);

    List<PostDto> getAllPosts();

    public List<PostDto> get5Posts();

    List<PostDto> getPostsByUserId(Long userId);

    public PostDto getPostById(Long PostId);

    PostDto updatePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException;

    void deletePost(Long id);

//    public Post findPostById(Long postId);
}
