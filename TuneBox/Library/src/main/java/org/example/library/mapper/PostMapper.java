package org.example.library.mapper;


import org.example.library.dto.PostDto;
import org.example.library.model.Post;

public class PostMapper {
    public static PostDto maptoPostDto(Post post) {
        return new PostDto(post.getId(), post.getContent(), post.getImages());
    }
    public static Post maptoPost(PostDto postDto) {
        return new Post(postDto.getId(), postDto.getContent(), postDto.getImages());
    }
}
