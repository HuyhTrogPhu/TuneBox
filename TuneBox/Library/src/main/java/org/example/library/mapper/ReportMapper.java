package org.example.library.mapper;

import org.example.library.dto.ReportDto;
import org.example.library.model.Post;
import org.example.library.model.Report;
import org.example.library.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public ReportDto toDto(Report report) {
        if (report == null) {
            return null;
        }
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setPostId(report.getPost().getId());
        dto.setUserId(report.getUser().getId());
        dto.setReason(report.getReason()); // Nếu bạn thêm trường lý do vào Report entity
        dto.setCreateDate(report.getCreateDate());
        dto.setStatus(report.getStatus()); // Trạng thái báo cáo
        return dto;
    }

    public Report toEntity(ReportDto dto) {
        if (dto == null) {
            return null;
        }
        Report report = new Report();
        report.setId(dto.getId());

        // Giả sử bạn đã có một phương thức trong PostService để tìm kiếm Post bằng ID
        Post post = new Post();
        post.setId(dto.getPostId());
        report.setPost(post);

        User user = new User();
        user.setId(dto.getUserId());
        report.setUser(user);

        report.setCreateDate(dto.getCreateDate());
        report.setStatus(dto.getStatus()); // Trạng thái báo cáo
        report.setReason(dto.getReason()); // Lý do báo cáo
        return report;
    }
}
