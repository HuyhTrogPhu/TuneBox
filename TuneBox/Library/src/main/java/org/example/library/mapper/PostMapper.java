package org.example.library.mapper;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;
import java.util.Set;

public class PostMapper {

    public static PostDto toDto(Post post) {
        if (post == null) return null;

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImages(PostImageMapper.toDtoSet(post.getImages()));  // Ánh xạ thủ công cho Set PostImage
        return dto;
    }

    public static Post toEntity(PostDto postDto) {
        if (postDto == null) return null;

        Post entity = new Post();
        entity.setId(postDto.getId());
        entity.setContent(postDto.getContent());
        entity.setImages(PostImageMapper.toEntitySet(postDto.getImages()));  // Ánh xạ thủ công cho Set PostImageDto
        return entity;
    }
}
