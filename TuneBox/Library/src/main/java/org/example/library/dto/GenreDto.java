package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private Long id;
    private String name;

    // Chỉ lưu trữ danh sách ID của các đối tượng liên quan thay vì lưu trữ toàn bộ đối tượng
    private Set<Long> userIds;
    private Set<Long> trackIds;
    private Set<Long> albumIds;

    public GenreDto(Long id, String name) {
    }
}
