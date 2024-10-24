package org.example.library.mapper;

import org.example.library.dto.PostImageDto;
import org.example.library.model.PostImage;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PostImageMapper {

    public static PostImageDto toDto(PostImage postImage) {
        if (postImage == null) return null;

        PostImageDto dto = new PostImageDto();
        dto.setId(postImage.getId());
        dto.setPostImage(postImage.getPostImage().getBytes());
        return dto;
    }

    public static PostImage toEntity(PostImageDto postImageDto) {
        if (postImageDto == null) return null;

        PostImage entity = new PostImage();
        entity.setId(postImageDto.getId());
        entity.setPostImage(Arrays.toString(postImageDto.getPostImage()));
        return entity;
    }

    public static Set<PostImageDto> toDtoSet(Set<PostImage> postImages) {
        if (postImages == null) return null;

        return postImages.stream()
                .map(PostImageMapper::toDto)
                .collect(Collectors.toSet());
    }

    public static Set<PostImage> toEntitySet(Set<PostImageDto> postImageDtos) {
        if (postImageDtos == null) return null;

        return postImageDtos.stream()
                .map(PostImageMapper::toEntity)
                .collect(Collectors.toSet());
    }
}