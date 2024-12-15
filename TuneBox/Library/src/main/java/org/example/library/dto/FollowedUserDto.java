package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowedUserDto {
    private Long id;
    private String userName;
    private String email;
    private String avatar;
    private boolean isFollowing;
}
