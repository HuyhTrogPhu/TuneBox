package org.example.library.mapper;

import org.example.library.dto.PostReportDto;
import org.example.library.model.PostReport;

public class PostReportMapper {

    // Chuyển đổi từ PostReport sang PostReportDto
    public static PostReportDto toDto(PostReport postReport) {
        if (postReport == null) {
            return null;
        }
        return new PostReportDto(
            postReport.getId(),
                postReport.getContent(),
                postReport.getReporter(),
            postReport.getReportedPostId(),
            postReport.getReason(),
            postReport.getDateReported(),
                checkSensitiveContent(postReport.getContent()) // Hàm kiểm tra nội dung nhạy cảm

        );
    }

    // Chuyển đổi từ PostReportDto sang PostReport
    public static PostReport toEntity(PostReportDto postReportDto) {
        if (postReportDto == null) {
            return null;
        }
        return new PostReport(
            postReportDto.getId(),
                postReportDto.getContent(),
                postReportDto.getReporter(),
            postReportDto.getReportedPostId(),
            postReportDto.getReason(),
            postReportDto.getDateReported()
        );
    }

    public static boolean checkSensitiveContent(String content) {
        String[] sensitiveWords = {"cc", "vai lol", "du ma", "dit"}; // Ví dụ
        for (String word : sensitiveWords) {
            if (content != null && content.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
