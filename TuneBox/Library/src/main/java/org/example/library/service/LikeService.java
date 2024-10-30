package org.example.library.service;

import org.example.library.dto.LikeDto;
import org.example.library.model.Like;

import java.util.List;

public interface LikeService {
    LikeDto addLike(Long userId, Long postId, Long trackId); // Hoặc void nếu không cần trả về
    void removeLike(Long userId, Long postId, Long trackId);
    List<LikeDto> getLikesByPostId(Long postId);
    List<LikeDto> getLikesByTrackId(Long trackId);
    boolean checkUserLike(Long postId, Long userId);
    boolean checkUserLikeTrack(Long trackId, Long userId);

    long countLikesByPostId(Long postId);
    long countLikesByTrackId(Long trackId);
    long countLikesByPlaylistId(Long playlistId);
    List<LikeDto> getAllByUserId(Long userId);


    List<LikeDto> getAllAlbumByUserId(Long userId);
    List<LikeDto> getAllPlayListByUserId(Long userId);
}
