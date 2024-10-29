package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {
    private Long id;
    private Long requesterId;
    private String requesterName;
    private String requesterUserNickName;
    private String requesterAvatar;


}
