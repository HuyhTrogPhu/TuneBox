package org.example.library.dto;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.CommentLike;
import org.example.library.model.Post;
import org.example.library.model.Track;
import org.example.library.model.User;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String content;

    private LocalDate creationDate;

    private Set<CommentLike> commentLikes;

    private Track track;

    private Post post;

    private User user;
}
