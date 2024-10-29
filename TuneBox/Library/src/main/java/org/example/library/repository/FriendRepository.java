package org.example.library.repository;

import org.example.library.dto.FriendAcceptDto;
import org.example.library.model.Friend;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // get friend list
    @Query("select new org.example.library.dto.FriendAcceptDto(u.id, ui.avatar, ui.name, u.userName)" +
            "from Friend f join f.user u join u.userInformation ui where u.id = :userId")
    List<FriendAcceptDto> getFriendAccepts(@Param("userId") Long userId);
}

