package org.example.library.repository;

import org.example.library.model.Chat;
import org.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findBySenderOrReceiverOrderByCreationDateDesc(User sender, User receiver);

    @Query("SELECT c FROM Chat c WHERE (c.sender = :user1 AND c.receiver = :user2) OR (c.sender = :user2 AND c.receiver = :user1)")
    Optional<Chat> findChatBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    List<Chat> findBySenderOrderByCreationDateDesc(User sender);

    List<Chat> findByReceiverOrderByCreationDateDesc(User receiver);

    @Query("SELECT c FROM Chat c WHERE (c.sender = :sender AND c.receiver = :receiver) OR (c.sender = :receiver AND c.receiver = :sender)")
    Optional<Chat> findBySenderAndReceiver(@Param("sender") User sender, @Param("receiver") User receiver);

}