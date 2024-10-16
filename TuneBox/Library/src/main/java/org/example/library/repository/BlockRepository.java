package org.example.library.repository;

import org.example.library.model.Block;
import org.example.library.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    boolean existsByBlocker_IdAndBlocked_Id(Long userId, Long targetUserId);

    // Tìm danh sách block theo ID của blocker
    List<Block> findByBlocker_Id(Long blockerId); // Sửa để gọi đúng theo ID

    // Tìm danh sách block theo ID của blocked
    List<Block> findByBlocked_Id(Long blockedId);

    Optional<Block> findByBlocker_IdAndBlocked_Id(Long blockerId, Long blockedId);

}
