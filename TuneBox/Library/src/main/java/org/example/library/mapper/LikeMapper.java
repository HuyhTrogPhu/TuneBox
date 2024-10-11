package org.example.library.mapper;

import org.example.library.dto.LikeDto;
import org.example.library.model.Like;


public class LikeMapper {
    public static LikeDto toDto(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());
        dto.setPostId(like.getPost().getId());
        dto.setTrackId(like.getTrack().getId());// Nếu bạn muốn thêm trackId cũng có thể thêm ở đây
        return dto;
    }

    public static LikeDto toDtoTrack(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());

        dto.setTrackId(like.getTrack().getId());
        return dto;
    }

    public static Like toEntity(LikeDto dto) {
        Like like = new Like();
        like.setId(dto.getId());
        like.setCreateDate(dto.getCreateDate());
        // Set user và post dựa trên id từ dto, có thể sử dụng UserRepository và PostRepository
        return like;
    }
}
