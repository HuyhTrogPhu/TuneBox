package org.example.library.mapper;

import org.example.library.dto.LikeDto;
import org.example.library.model.Albums;
import org.example.library.model.Like;
import org.example.library.model.Playlist;
import org.example.library.model.User;


public class LikeMapper {
    public static LikeDto toDto(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());

        if (like.getPost() != null) {
            dto.setPostId(like.getPost().getId());
        }

        if (like.getTrack() != null) {
            dto.setTrackId(like.getTrack().getId());
        }

//        if (like.getComment() != null) {
//            dto.setCommentId(like.getComment().getId());
//        }

        return dto;
    }

    public static LikeDto toDtoTrack(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());

        dto.setTrackId(like.getTrack().getId());
        return dto;
    }

    public static Like toEntity(LikeDto dto) {
        Like like = new Like();
        like.setId(dto.getId());
        like.setCreateDate(dto.getCreateDate());
        // Set user và post dựa trên id từ dto, có thể sử dụng UserRepository và PostRepository
        return like;
    }


    // với cả Track và Post
    public static LikeDto PostAndTrack(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());

        // Thiết lập ID bài viết nếu có
        if (like.getPost() != null) {
            dto.setPostId(like.getPost().getId());
        }

        // Thiết lập ID track nếu có
        if (like.getTrack() != null) {
            dto.setTrackId(like.getTrack().getId());
        }

        return dto;
    }

// thiet lap Album
    public static LikeDto toAlbumDto(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());

        if (like.getAlbums() != null) {
            dto.setAlbumId(like.getAlbums().getId());
        }

        return dto;
    }

    public static Like toAlbum(LikeDto dto) {
        Like like = new Like();
        like.setId(dto.getId());
        like.setCreateDate(dto.getCreateDate());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            like.setUser(user); // Thiết lập user cho Like
        }

        if (dto.getAlbumId() != null) {
            Albums album = new Albums();
            album.setId(dto.getAlbumId());
            like.setAlbums(album); // Thiết lập album cho Like
        }

        return like;
    }

    // thiet lap playlist
    public static LikeDto toPlayListDto(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());

        if (like.getPlaylist() != null) {
            dto.setPlaylistId(like.getPlaylist().getId());
        }

        return dto;
    }

    public static Like toPlayList(LikeDto dto) {
        Like like = new Like();
        like.setId(dto.getId());
        like.setCreateDate(dto.getCreateDate());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            like.setUser(user); // Thiết lập user cho Like
        }

        if (dto.getPlaylistId() != null) {
            Playlist playlist = new Playlist();
            playlist.setId(dto.getPlaylistId());
            like.setPlaylist(playlist); // Thiết lập laylist cho Like
        }

        return like;
    }


    public static LikeDto toDtoPlaylist(Like like) {
        LikeDto dto = new LikeDto();
        dto.setId(like.getId());
        dto.setCreateDate(like.getCreateDate());
        dto.setUserId(like.getUser().getId());

        if (like.getPlaylist() != null) {
            dto.setPlaylistId(like.getPlaylist().getId());
        }

        return dto;
    }

}
