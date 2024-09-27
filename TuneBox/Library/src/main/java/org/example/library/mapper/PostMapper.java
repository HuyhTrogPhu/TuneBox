package org.example.library.mapper;

import org.example.library.dto.PostDto;
import org.example.library.dto.PostImageDto;
import org.example.library.model.Post;
import org.example.library.model.PostImage;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    // Chuyển từ Post sang PostDto
    public static PostDto mapToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());

        // Map Set<PostImage> thành Set<PostImageDto>
        if (post.getImages() != null) {
            Set<PostImageDto> postImageDtos = post.getImages().stream()
                    .map(PostImageMapper::mapToPostImageDto)
                    .collect(Collectors.toSet());
            postDto.setImages(postImageDtos);
        }

        return postDto;
    }

    // Chuyển từ PostDto sang Post
    public static Post mapToPost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setContent(postDto.getContent());

        // Map Set<PostImageDto> thành Set<PostImage>
        if (postDto.getImages() != null) {
            Set<PostImage> postImages = postDto.getImages().stream()
                    .map(PostImageMapper::mapToPostImage)
                    .collect(Collectors.toSet());
            post.setImages(postImages);
        }

        return post;
    }
}
