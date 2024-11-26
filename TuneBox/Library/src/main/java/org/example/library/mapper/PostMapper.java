package org.example.library.mapper;

import org.example.library.dto.PostDto;
import org.example.library.model.Post;


public class PostMapper {

    public static PostDto toDto(Post post) {
        if (post == null) return null;

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setHidden(post.isHidden());
        dto.setAdminHidden(post.isAdminHidden());
        dto.setContent(post.getContent());
        dto.setUserId(post.getUser() != null ? post.getUser().getId() : null);
        dto.setAvatar(post.getUser().getUserInformation().getAvatar());
        dto.setUserNickname(post.getUser() != null ? post.getUser().getUserInformation().getName() : null);
        dto.setImages(PostImageMapper.toDtoSet(post.getImages()));
        dto.setCreatedAt(post.getCreatedAt());
//        dto,setDescription(post.getDescription());
        dto.setLikeCount(post.getLikes() != null ? post.getLikes().size() : 0);
        dto.setCommentCount(post.getComments() != null ? post.getComments().size() : 0);
        return dto;
    }


    public static Post toEntity(PostDto postDto) {
        if (postDto == null) return null;

        Post entity = new Post();
        entity.setId(postDto.getId());
        entity.setHidden(postDto.isHidden());
        entity.setContent(postDto.getContent());
        entity.setImages(PostImageMapper.toEntitySet(postDto.getImages()));
        entity.setCreatedAt(postDto.getCreatedAt());
        return entity;
    }
}