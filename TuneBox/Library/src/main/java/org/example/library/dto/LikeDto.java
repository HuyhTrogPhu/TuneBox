package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {
    private Long id;
    private LocalDate createDate;
    private Long userId;
    private Long postId;
    private Long trackId;


}
