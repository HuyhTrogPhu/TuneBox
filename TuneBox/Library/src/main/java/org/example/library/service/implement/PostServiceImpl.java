package org.example.library.service.implement;

import org.example.library.dto.PostDto;
import org.example.library.mapper.PostMapper;
import org.example.library.model.Post;
import org.example.library.model.PostImage;
import org.example.library.repository.PostRepository;
import org.example.library.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public PostDto savePost(PostDto postDto, MultipartFile[] images) throws IOExceptiond {
        Post post = PostMapper.mapToPost(postDto);

        // Chuyển đổi MultipartFile[] thành PostImage[]
        Set<PostImage> postImages = new HashSet<>();
        for (MultipartFile image : images) {
            PostImage postImage = new PostImage();
            postImage.setPost(post); // Gắn liên kết với Post
            postImage.setPostImage(image.getBytes()); // Lưu hình ảnh dưới dạng byte[]
            postImages.add(postImage);
        }
        post.setImages(postImages);

        Post savedPost = postRepository.save(post);
        return PostMapper.mapToPostDto(savedPost);
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return PostMapper.mapToPostDto(post);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto, MultipartFile[] images) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setContent(postDto.getContent());

        // Cập nhật hình ảnh nếu có
        if (images != null) {
            Set<PostImage> postImages = new HashSet<>();
            for (MultipartFile image : images) {
                PostImage postImage = new PostImage();
                postImage.setPost(post);
                postImage.setPostImage(image.getBytes());
                postImages.add(postImage);
            }
            post.setImages(postImages);
        }

        Post updatedPost = postRepository.save(post);
        return PostMapper.mapToPostDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }
}
