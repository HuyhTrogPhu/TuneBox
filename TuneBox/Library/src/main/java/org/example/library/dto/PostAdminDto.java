package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAdminDto {
    private Long id;                      // ID của bài đăng
    private String content;               // Nội dung bài đăng
    private String userName;              // Tên người dùng đã tạo bài đăng
    private LocalDateTime createdAt;      // Thời gian tạo bài đăng
    private long likeCount;               // Số lượt like
    private long commentCount;            // Số lượng bình luận
    private String description;           // Mô tả bài đăng
    private boolean hidden;               // Trạng thái bài đăng
}
