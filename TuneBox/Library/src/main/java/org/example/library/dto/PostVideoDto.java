package org.example.library.dto;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Post;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostVideoDto {

    private Long id;

    private String urlVideo;

    private Post post;
}
