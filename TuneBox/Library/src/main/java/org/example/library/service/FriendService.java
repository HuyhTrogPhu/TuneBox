package org.example.library.service;

import org.example.library.dto.FriendRequestDTO;
import org.example.library.model.User;

import java.util.List;

public interface FriendService {
    Long sendFriendRequest(Long userId, Long friendId);
    void acceptFriendRequest(Long requestId);
    void declineFriendRequest(Long requestId);
    void unfriend(Long userId, Long friendId);

    void cancelFriendRequest(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    String checkFriendStatus(Long userId, Long friendId);

    List<FriendRequestDTO> getPendingFriendRequests(Long userId);

    Long countFriends(Long userId);
}
