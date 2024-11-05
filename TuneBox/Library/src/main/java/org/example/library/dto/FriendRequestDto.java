package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    private Long requestId;
    private Long senderId;
    private Long receiverId;
    private boolean isSender; // true nếu là người gửi, false nếu là người nhận
    private String status;
    private String senderAvatar;
    private String senderName;

}
