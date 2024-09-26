package org.example.library.service.implement;

import org.example.library.dto.PostDto;
import org.example.library.mapper.PostMapper;
import org.example.library.model.Post;
import org.example.library.model.PostImage;
import org.example.library.repository.PostRepository;
import org.example.library.service.PostService;
import org.example.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageUpload imageUpload;

    @Override
    public PostDto savePost(PostDto postDto, MultipartFile[] images) {
        try {
            Post post = PostMapper.maptoPost(postDto);
            Set<PostImage> postImages = new HashSet<>();

            // Upload từng file ảnh
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    PostImage postImage = new PostImage();
                    // Tải ảnh lên và lưu trữ dưới dạng byte[] trong PostImage
                    postImage.setPostImage(image.getBytes());
                    postImage.setPost(post);
                    postImages.add(postImage);
                }
            }

            // Gán Set PostImage vào Post
            post.setImages(postImages);

            post.setContent(postDto.getContent());
            Post savedPost = postRepository.save(post);
            return PostMapper.maptoPostDto(savedPost);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto, List<MultipartFile> images) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Post not found")
        );

        // Handle image updates
        Set<PostImage> postImages = new HashSet<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!imageUpload.checkExist(image)) {
                    imageUpload.uploadFile(image);
                    PostImage postImage = new PostImage();
                    postImage.setPost(post); // Set the relationship with Post
                    postImages.add(postImage);
                }
            }
        }

        post.setImages(postImages); // Update the images in the Post entity
        post.setContent(postDto.getContent());
        Post savedPost = postRepository.save(post);
        return PostMapper.maptoPostDto(savedPost);
    }


    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Post not found")
        );
        return PostMapper.maptoPostDto(post);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostMapper::maptoPostDto).collect(Collectors.toList());
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Post not found")
        );
        postRepository.deleteById(post.getId());
    }
}