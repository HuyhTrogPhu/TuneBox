package org.example.library.service;

import org.example.library.dto.LikeDto;
import org.example.library.model.Like;
import org.example.library.model.Post;

import java.util.List;

public interface LikeService {
    LikeDto addLikePlaylist(Long userId, Long playlistId);
    LikeDto addLikeAlbum(Long userId, Long albumId);
    LikeDto addLike(Long userId, Long postId, Long trackId);
    // Hoặc void nếu không cần trả về
    void removeLike(Long userId, Long postId, Long trackId);
    void removeLikePlaylist(Long userId, Long playlistID);
    void removeLikeAlbum(Long userId, Long albumID);

    List<LikeDto> getLikesByPostId(Long postId);
    List<LikeDto> getLikesByTrackId(Long trackId);
    boolean checkUserLike(Long postId, Long userId);
    boolean checkUserLikeTrack(Long trackId, Long userId);
    boolean checkUserLikePlaylist(Long playlistId,Long userId);
    boolean checkUserLikeAlbum(Long albumId,Long userId);

    long countLikesByPostId(Long postId);
    long countLikesByTrackId(Long trackId);
    long countLikesByPlaylistId(Long playlistId);
    long countLikesByAlbumId(Long albumId);

    List<LikeDto> getAllByUserId(Long userId);
    List<LikeDto> getAllAlbumByUserId(Long userId);
    List<LikeDto> getAllPlayListByUserId(Long userId);

    List<Post> getLikedPostsByUser(Long userId);
}
