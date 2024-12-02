package org.example.library.service;

import org.example.library.dto.PostDto;
import org.example.library.dto.Report2Dto;
import org.example.library.dto.ReportDto;
import org.example.library.model.Post;
import org.example.library.dto.ReportDtoSocialAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.library.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    boolean checkReportExists(Long userId, Long postId, Long trackId, Long albumId, String type);

    ReportDto createReport(ReportDto reportDto);

    ReportDto getReportById(Long id);

    List<ReportDto> getAllReports();


    Report2Dto resolveReport(Long reportId, boolean hidePost);
//    Report2Dto resolveReport(Long reportId, boolean hidePost);

    Report2Dto getReport2ById(Long id);

    Report2Dto dismissReport(Long reportId, String reason);

    ReportDto updateApprove(Long id);
    List<ReportDto> updateApproveTrackId(Long id);
    List<ReportDto> updateApproveAlbumId(Long id);
    ReportDto updateDenied(Long id);
    List<ReportDto> updateDeniedTrackId(Long id);
    List<ReportDto> updateDeniedAlbumId(Long id);
    Page<ReportDtoSocialAdmin> findAllReportsWithTracks(Pageable pageable);
    Page<ReportDtoSocialAdmin> findAllReportsWithAlbum(Pageable pageable);
    Page<ReportDtoSocialAdmin> findAllReportsWithPost(Pageable pageable);
    Page<ReportDtoSocialAdmin> findAllReportsWithUser(Pageable pageable);
     ReportDtoSocialAdmin findById(Long id);
    List<ReportDtoSocialAdmin> findByTrackId(Long id);
    List<ReportDtoSocialAdmin> findByAlbumId(Long id);
    List<ReportDtoSocialAdmin> findByReportedId(Long id);
    void restorePost(Long reportId);


//    Report2Dto dismissReport(Long reportId, String reason);

    Page<Report2Dto> getPendingReportsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Report2Dto> getAllPendingReports(Pageable pageable);
    Page<Report2Dto> getPendingReportsBySpecificDate(LocalDate specificDate, Pageable pageable);

//    Report2Dto resolveReport(Long reportId, boolean hidePost, boolean fullClose);
//    List<Report2Dto> dismissAllReports(Long postId, String reason);
//    void restorePost(Long reportId);

    List<PostDto> getAdminHiddenAndResolvedPosts();

}