package org.example.library.mapper;

import org.example.library.dto.PostImageDto;
import org.example.library.model.PostImage;

public class PostImageMapper {

    // Chuyển từ PostImage entity sang PostImageDto
    public static PostImageDto mapToPostImageDto(PostImage postImage) {
        PostImageDto postImageDto = new PostImageDto();
        postImageDto.setId(postImage.getId());
        postImageDto.setPostImage(postImage.getPostImage());
        return postImageDto;
    }

    // Chuyển từ PostImageDto sang PostImage entity
    public static PostImage mapToPostImage(PostImageDto postImageDto) {
        PostImage postImage = new PostImage();
        postImage.setId(postImageDto.getId());
        postImage.setPostImage(postImageDto.getPostImage());
        return postImage;
    }
}
