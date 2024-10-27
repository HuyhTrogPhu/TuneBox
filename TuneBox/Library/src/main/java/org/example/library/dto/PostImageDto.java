package org.example.library.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.Post;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostImageDto {
    private Long id;
    private String postImage;
}
