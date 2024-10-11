package org.example.library.mapper;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;

public class PostMapper {

    public static PostDto toDto(Post post) {
        if (post == null) return null;

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setUserId(post.getUser() != null ? post.getUser().getId() : null); // Lấy ID người dùng
        dto.setUserName(post.getUser() != null ? post.getUser().getUserName() : null);
        dto.setImages(PostImageMapper.toDtoSet(post.getImages()));  // Ánh xạ thủ công cho Set PostImage
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    public static Post toEntity(PostDto postDto) {
        if (postDto == null) return null;

        Post entity = new Post();
        entity.setId(postDto.getId());
        entity.setContent(postDto.getContent());
        entity.setImages(PostImageMapper.toEntitySet(postDto.getImages()));  // Ánh xạ thủ công cho Set PostImageDto
        entity.setCreatedAt(postDto.getCreatedAt());
        return entity;
    }
}
