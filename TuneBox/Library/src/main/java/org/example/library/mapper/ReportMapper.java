package org.example.library.mapper;

import org.example.library.dto.ReportDto;
import org.example.library.model.*;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public ReportDto toDto(Report report) {
        if (report == null) {
            return null;
        }
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setPostId(report.getPost() != null ? report.getPost().getId() : null);
        dto.setTrackId(report.getTrack() != null ? report.getTrack().getId() : null); // Set trackId nếu có
        dto.setAlbumId(report.getAlbum() != null ? report.getAlbum().getId() : null); // Set albumId nếu có
//        dto.setPostId(report.getPost());
        dto.setUserId(report.getUser().getId());
        dto.setReason(report.getReason());
        dto.setCreateDate(report.getCreateDate());
        dto.setStatus(report.getStatus());
        dto.setType(report.getType());
        return dto;
    }

    public Report toEntity(ReportDto dto) {
        if (dto == null) {
            return null;
        }
        Report report = new Report();
        report.setId(dto.getId());

        // Ánh xạ Post
        if (dto.getPostId() != null) {
            Post post = new Post();
            post.setId(dto.getPostId());
            report.setPost(post);
        }

        // Ánh xạ Track
        if (dto.getTrackId() != null) {
            Track track = new Track();
            track.setId(dto.getTrackId());
            report.setTrack(track);
        }

        // Ánh xạ Album
        if (dto.getAlbumId() != null) {
            Albums album = new Albums();
            album.setId(dto.getAlbumId());
            report.setAlbum(album);
        }

        User user = new User();
        user.setId(dto.getUserId());
        report.setUser(user);

        report.setCreateDate(dto.getCreateDate());
        report.setStatus(dto.getStatus());
        report.setReason(dto.getReason());
        report.setType(dto.getType());
        return report;
    }

    public Report2Dto toReport2Dto(Report report) {
        if (report == null) {
            return null;
        }
        Report2Dto dto = new Report2Dto();
        dto.setId(report.getId());

        PostThisIdDto postDto = new PostThisIdDto();
        postDto.setPostId(report.getPost().getId());
        postDto.setPostOwner(report.getPost().getUser().getId());
        postDto.setHidden(report.getPost().isHidden());
        dto.setPost(postDto);
        dto.setReporterId(report.getUser().getId());
        dto.setReason(report.getReason());
        dto.setCreateDate(report.getCreateDate());
        dto.setStatus(report.getStatus());
        dto.setDescription(report.getDescription());
        return dto;
    }

    public ReportDto3 toReportDto3(Report report) {
        if (report == null) {
            return null;
        }
        ReportDto3 dto = new ReportDto3();
        dto.setId(report.getId());

        ReportedDto postDto = new ReportedDto();
        postDto.setReportedId(report.getPost().getId());
        dto.setPost(postDto);
        dto.setUserId(report.getUser().getId());
        dto.setReason(report.getReason());
        dto.setCreateDate(report.getCreateDate());
        dto.setStatus(report.getStatus());
        dto.setDescription(report.getDescription());
        return dto;
    }

    public Report toEntity(ReportDto3 dto) {
        if (dto == null) {
            return null;
        }

        Report report = new Report();
        report.setId(dto.getId());

        // Giả sử bạn có phương thức trong PostService để tìm kiếm Post bằng ID
        Post post = new Post();
        post.setId(dto.getPost().getReportedId());
        report.setPost(post);

        User user = new User();
        user.setId(dto.getUserId());
        report.setUser(user);

        report.setCreateDate(dto.getCreateDate());
        report.setStatus(dto.getStatus());
        report.setReason(dto.getReason());
        report.setDescription(dto.getDescription());

        return report;
    }
}
