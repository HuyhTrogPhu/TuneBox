package org.example.library.model_enum;

public enum FriendStatus {
    PENDING_SENT, // Yêu cầu đã gửi nhưng chưa được chấp nhận
    PENDING_RECEIVED, // Yêu cầu đã nhận nhưng chưa được chấp nhận
    ACCEPTED, // Bạn bè
    BLOCKED // Đã chặn
}

