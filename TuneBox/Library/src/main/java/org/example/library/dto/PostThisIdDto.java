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
    private Long postOwner;
    private boolean hidden;
}
