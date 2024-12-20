package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.model.User;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostThisIdDto {
    private Long postId;
    private String postOwner;
    private boolean hidden;
    private String content;
}
