package org.example.library.service;

import org.example.library.dto.LikeDto;

import java.util.List;

public interface LikeService {
    LikeDto addLike(Long userId, Long postId); // Hoặc void nếu không cần trả về
    void removeLike(Long userId, Long postId);
    List<LikeDto> getLikesByPostId(Long postId);
    boolean checkUserLike(Long postId, Long userId);
}
