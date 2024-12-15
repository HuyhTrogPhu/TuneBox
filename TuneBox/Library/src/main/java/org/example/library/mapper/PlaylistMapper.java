package org.example.library.mapper;

import org.example.library.dto.PlaylistDto;
import org.example.library.model.*;

import java.util.stream.Collectors;

public class PlaylistMapper {

    // Phương thức chuyển đổi từ playList sang playlistDto
    public static PlaylistDto mapperPlaylistDto(Playlist playlist) {
        return new PlaylistDto(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getImagePlaylist(),
                playlist.getDescription(),
                playlist.getType(),
                playlist.getCreateDate(),
                playlist.getReportDate(),
                playlist.isReport(),
                playlist.isStatus(),
                playlist.getCreator() != null ? playlist.getCreator().getId() : null,  // Creator ID
                playlist.getTracks() != null ? playlist.getTracks().stream().map(Track::getId).collect(Collectors.toSet()) : null  // Set of track IDs
        );
    }

    // Phương thức chuyển đổi từ PlaylistDto sang playlist
    public static Playlist mapperPlaylist(PlaylistDto playlistDto) {
        Playlist playlist = new Playlist();
        playlist.setId(playlistDto.getId());
        playlist.setTitle(playlistDto.getTitle());
        playlist.setImagePlaylist(playlistDto.getImagePlaylist());
        playlist.setDescription(playlistDto.getDescription());
        playlist.setType(playlistDto.getType());
        playlist.setCreateDate(playlistDto.getCreateDate());
        playlist.setReportDate(playlistDto.getReportDate());
        playlist.setReport(playlistDto.isStatus());
        playlist.setStatus(playlist.isStatus());

        // Thiết lập Creator từ albumsDto
        if (playlistDto.getCreatorId() != null) {
            User creator = new User();
            creator.setId(playlistDto.getCreatorId());
            playlist.setCreator(creator);  // Assume Creator is already fetched or managed elsewhere
        }

        return playlist;
    }

}
