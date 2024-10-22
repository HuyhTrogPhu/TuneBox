package org.example.library.mapper;

import org.example.library.dto.FollowDto;
import org.example.library.model.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {

    // Chuyển từ entity Follow sang DTO
    public FollowDto toDto(Follow follow) {
        return new FollowDto(
                follow.getId(),
                follow.getFollower().getId(),
                follow.getFollowed().getId(),
                follow.getCreatedAt()
        );
    }

    // Chuyển từ DTO sang entity Follow
    public Follow toEntity(FollowDto followDto) {
        Follow follow = new Follow();
        follow.setId(followDto.getId());
        return follow;
    }
}
