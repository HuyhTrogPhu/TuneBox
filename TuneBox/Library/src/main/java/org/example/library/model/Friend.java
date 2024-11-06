package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // User who sent the friend request
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)  // User who received the friend request
    private User friend;

    private boolean accepted; // Trạng thái chấp nhận

    private String status; // Trạng thái lời mời (ví dụ: "pending", "declined")

}
