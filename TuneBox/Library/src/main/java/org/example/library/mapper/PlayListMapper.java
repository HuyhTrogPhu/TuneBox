package org.example.library.mapper;

import org.example.library.dto.PlaylistDto;
import org.example.library.model.Playlist;

public class PlayListMapper {
    public Playlist mapToEntity(PlaylistDto playlist){
      return new Playlist(
              playlist.getId(),
              playlist.getTitle(),
              playlist.getImagePlaylist(),
              playlist.getDescription(),
              playlist.getType(),
              playlist.getCreateDate(),
              playlist.isReport(),
              playlist.getReportDate(),
              playlist.getTracks()
      );
    }


    public static PlaylistDto mapToDto(Playlist playlist){
        return new PlaylistDto(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getImagePlaylist(),
                playlist.getDescription(),
                playlist.getType(),
                playlist.getCreateDate(),
                playlist.isReport(),
                playlist.getReportDate(),
                playlist.getTracks()
        );
    }
}
