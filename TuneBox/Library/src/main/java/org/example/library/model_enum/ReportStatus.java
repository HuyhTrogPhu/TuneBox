package org.example.library.model_enum;

public enum ReportStatus {
    PENDING("Đang chờ xử lý"),    // Báo cáo mới, chưa được xem xét
    RESOLVED("Đã xử lý"),         // Đã xử lý và có hành động thích hợp
    DISMISSED("Đã bác bỏ");     // Báo cáo không hợp lệ
    private final String description;


    ReportStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}