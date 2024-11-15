package org.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model_enum.MessageStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "content")
    private String content;

    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OtherAttachment> attachments;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT;

    private LocalDateTime revokedAt;

}