package org.example.library.mapper;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;

public class PostMapper {

    public static PostDto toDto(Post post) {
        if (post == null) return null;

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setHidden(post.isHidden());
        dto.setContent(post.getContent());
        dto.setUserId(post.getUser() != null ? post.getUser().getId() : null); // Lấy ID người dùng

        // Kiểm tra post.getUser() không null trước khi lấy userName
        dto.setUserNickname(post.getUser() != null ? post.getUser().getUserInformation().getName() : null);

        dto.setImages(PostImageMapper.toDtoSet(post.getImages()));  // Ánh xạ thủ công cho Set PostImage
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    public static Post toEntity(PostDto postDto) {
        if (postDto == null) return null;

        Post entity = new Post();
        entity.setId(postDto.getId());
        entity.setHidden(postDto.isHidden());
        entity.setContent(postDto.getContent());
        entity.setImages(PostImageMapper.toEntitySet(postDto.getImages()));  // Ánh xạ thủ công cho Set PostImageDto
        entity.setCreatedAt(postDto.getCreatedAt());
        return entity;
    }
}
