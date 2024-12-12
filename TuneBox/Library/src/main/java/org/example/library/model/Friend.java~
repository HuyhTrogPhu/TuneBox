package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model_enum.FriendStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friends", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_friend_id", columnList = "friend_id")
})
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // User who sent the friend request
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)  // User who received the friend request
    private User friend;

    private boolean accepted; // Trạng thái chấp nhận

    @Enumerated(EnumType.STRING)
    private FriendStatus status;
}
