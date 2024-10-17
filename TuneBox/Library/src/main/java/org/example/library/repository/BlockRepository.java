package org.example.library.repository;

import org.example.library.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByBlocker_IdAndBlocked_Id(Long blockerId, Long blockedId);

    boolean existsByBlocker_IdAndBlocked_Id(Long blockerId, Long blockedId);

    void deleteByBlocker_IdAndBlocked_Id(Long blockerId, Long blockedId);

    List<Block> findByBlocker_Id(Long blockerId);
}

