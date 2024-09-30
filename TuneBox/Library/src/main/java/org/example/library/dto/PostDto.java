package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.model.PostImage;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String content;
    private Long userId; // Trường userId
    private String userName; // Thêm trường username
    private Set<PostImageDto> images;

}