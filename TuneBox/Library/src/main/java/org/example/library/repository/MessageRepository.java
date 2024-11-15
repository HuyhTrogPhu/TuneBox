package org.example.library.repository;

import org.example.library.model.Chat;
import org.example.library.model.Message;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
            "ORDER BY m.dateTime")
    List<Message> findMessagesBetween(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN Friend f ON (f.user.id = :userId AND f.friend.id = u.id) OR (f.friend.id = :userId AND f.user.id = u.id) " +
            "WHERE f.status = 'ACCEPTED' AND f.accepted = true")
    List<User> findAcceptedFriends(@Param("userId") Long userId);

}

