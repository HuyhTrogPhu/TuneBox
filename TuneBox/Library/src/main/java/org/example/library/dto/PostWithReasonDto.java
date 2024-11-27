package org.example.library.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.mapper.PostMapper;
import org.example.library.model.Post;

@Data
@NoArgsConstructor
public class PostWithReasonDto {
    private PostDto post;
    private String reason;

    public PostWithReasonDto(Post post, String reason) {
        this.post = PostMapper.toDto(post);
        this.reason = reason;
    }

}
