package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id")
    private Long id;

    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Message> messages;


}