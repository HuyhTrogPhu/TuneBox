package org.example.library.service;

import org.example.library.dto.FriendAcceptDto;
import org.example.library.dto.FriendRequestDto;

import java.util.List;
import java.util.Map;

public interface FriendService {
    Long sendFriendRequest(Long userId, Long friendId);
    void acceptFriendRequest(Long requestId);
    void declineFriendRequest(Long requestId);
    void unfriend(Long userId, Long friendId);

    void cancelFriendRequest(Long userId, Long friendId);

    List<FriendAcceptDto> getFriends(Long userId);

    Map<String, Object> checkFriendStatus(Long userId, Long friendId);

    List<FriendRequestDto> getFriendRequests(Long userId);

    Long countFriends(Long userId);
}
