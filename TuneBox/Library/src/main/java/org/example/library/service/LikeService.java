package org.example.library.service;

import org.example.library.dto.LikeDto;

import java.util.List;

public interface LikeService {
    LikeDto addLike(Long userId, Long postId, Long trackId); // Hoặc void nếu không cần trả về
    void removeLike(Long userId, Long postId, Long trackId);
    List<LikeDto> getLikesByPostId(Long postId);
    List<LikeDto> getLikesByTrackId(Long trackId);
    boolean checkUserLike(Long postId, Long userId);
    boolean checkUserLikeTrack(Long trackId, Long userId);
}
