package org.example.library.repository;

import org.example.library.model.Friend;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUserAndFriend(User user, User friend);
    Optional<Friend> findByFriendAndUser(User friend, User user);
    boolean existsByUserAndFriend(User user, User friend);
    List<Friend> findByFriendAndStatus(User friend, String status);
    List<Friend> findByUserAndStatus(User user, String status);
    // Đếm số lượng bạn bè từ phía user
    Long countByUserAndStatus(User user, String status);

    // Đếm số lượng bạn bè từ phía người khác
    Long countByFriendAndStatus(User friend, String status);
}

