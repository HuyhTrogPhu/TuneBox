package org.example.library.repository;

import org.example.library.dto.FriendAcceptDto;
import org.example.library.model.Friend;
import org.example.library.model.User;
import org.example.library.model_enum.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUserAndFriend(User user, User friend);
    Optional<Friend> findByFriendAndUser(User friend, User user);
    List<Friend> findByUserAndStatus(User user, FriendStatus status);
    List<Friend> findByFriendAndStatus(User friend, FriendStatus status);
    // get friend list
    @Query("select new org.example.library.dto.FriendAcceptDto(u.id,f.id, ui.avatar, ui.name, u.userName)" +
            "from Friend f join f.friend u join u.userInformation ui where f.user.id = :userId and f.accepted = true ")
    List<FriendAcceptDto> getFriendAccepts(@Param("userId") Long userId);

    List<Friend> findByFriendId(Long userId);

    Optional<Friend> findByUserAndFriendAndStatus(User user, User friend, FriendStatus friendStatus);

    long countByUserId(Long id);
}

