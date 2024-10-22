package org.example.library.service;

import org.example.library.model.Friend;

import java.util.List;

public interface FriendService {
    void sendFriendRequest(Long userId, Long friendId);
    void acceptFriendRequest(Long requestId);
    void declineFriendRequest(Long requestId);
    void unfriend(Long userId, Long friendId);

    void cancelFriendRequest(Long userId, Long friendId);

    String checkFriendStatus(Long userId, Long friendId);

    List<Friend> getPendingFriendRequests(Long userId);
}
