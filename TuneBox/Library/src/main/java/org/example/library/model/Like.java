package org.example.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id", "comment_id", "reply_id"})})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDate createDate = LocalDate.now(); // Auto-set current date

    @ManyToOne
    @JoinColumn(name = "track_id")
    @JsonIgnore
    private Track track;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment; // Thêm trường này cho bình luận

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply; // Thêm trường này cho phản hồi


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Albums albums;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;


}