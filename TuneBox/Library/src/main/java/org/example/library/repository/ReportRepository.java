package org.example.library.repository;

import org.example.library.model.Post;
import org.example.library.model.Report;
import org.example.library.model_enum.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>  {
    List<Report> findAll(); // Truy xuất tất cả các báo cáo
    List<Report> findByStatus(ReportStatus status); // Phương thức này đã có
    List<Report> findByPost(Post post);
    List<Report> findByPostIdAndStatus(Long postId, ReportStatus status);
//    @Query("SELECT r FROM Report r WHERE r.status = :status ORDER BY r.createDate DESC")
// Nếu bạn muốn lấy các bài đăng bị báo cáo theo status
//    List<Post> findDistinctPostsByStatus(ReportStatus status);
}
