package org.example.library.service.implement;

import org.example.library.dto.PostDto;
import org.example.library.mapper.PostMapper;
import org.example.library.model.Post;
import org.example.library.model.PostImage;
import org.example.library.repository.PostRepository;
import org.example.library.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto savePost(PostDto postDto, MultipartFile[] images) throws IOException {
        if (postDto.getContent() == null || postDto.getContent().isEmpty()) {
            throw new IllegalArgumentException("Post content must not be empty");
        }

        Post post = PostMapper.toEntity(postDto); // Chuyển đổi PostDto thành Post entity

        // Xử lý hình ảnh nếu có
        if (images != null && images.length > 0) {
            Set<PostImage> postImages = new HashSet<>();
            for (MultipartFile image : images) {
                // Kiểm tra nếu tệp hình ảnh không rỗng và là hình ảnh
                if (image != null && !image.isEmpty()) {
                    PostImage postImage = new PostImage();
                    postImage.setPost(post);  // Thiết lập quan hệ với Post
                    postImage.setPostImage(image.getBytes()); // Lưu hình ảnh dưới dạng byte[]
                    postImages.add(postImage);
                }
            }
            post.setImages(postImages);
        }

        // Lưu post vào database
        Post savedPost = postRepository.save(post);

        // Chuyển Post entity thành PostDto và trả về
        return PostMapper.toDto(savedPost);
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        return PostMapper.toDto(post);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll(); // Lấy tất cả các bài viết từ repository
        return posts.stream()
                .map(PostMapper::toDto) // Chuyển đổi thành PostDto
                .collect(Collectors.toList());
    }

}
