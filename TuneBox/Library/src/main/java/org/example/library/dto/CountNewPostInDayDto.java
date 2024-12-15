package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountNewPostInDayDto {
    private Long count; // Số lượng bài viết
    private List<PostDto> posts; // Danh sách bài viết
}
