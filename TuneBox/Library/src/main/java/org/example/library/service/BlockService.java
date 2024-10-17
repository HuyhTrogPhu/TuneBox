package org.example.library.service;

import org.example.library.dto.BlockDto;

import java.util.List;

public interface BlockService {
    void blockUser(Long userId, Long targetUserId);
    void unblockUser(Long userId, Long targetUserId);
    boolean isUserBlocked(Long userId, Long targetUserId);
    List<BlockDto> getBlockedUsers(Long userId);
}
