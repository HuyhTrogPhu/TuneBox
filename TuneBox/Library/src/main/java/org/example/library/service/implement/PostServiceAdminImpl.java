package org.example.library.service.implement;

import org.example.library.dto.PostAdminDto;
import org.example.library.model.Post;
import org.example.library.repository.PostAdminRepository;
import org.example.library.service.PostServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceAdminImpl implements PostServiceAdmin {

    @Autowired
    private PostAdminRepository postAdminRepository;

    @Override
    public List<PostAdminDto> getTopPostsByInteractions() {
        List<Post> posts = postAdminRepository.findTopPostsByInteractions();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findPostsWithImages() {
        List<Post> posts = postAdminRepository.findPostsWithImages();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findPostsWithoutImages() {
        List<Post> posts = postAdminRepository.findPostsWithoutImages();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PostAdminDto> findAllByOrderByCreatedAtDesc() {
        List<Post> posts = postAdminRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private PostAdminDto convertToDto(Post post) {
        return new PostAdminDto(
                post.getId(),
                post.getContent(),
                post.getUser().getUserName(),
                post.getCreatedAt(),
                post.getLikes().size(),
                post.getComments().size(),
                post.getDescription(),
                post.isHidden()
        );
    }
}
