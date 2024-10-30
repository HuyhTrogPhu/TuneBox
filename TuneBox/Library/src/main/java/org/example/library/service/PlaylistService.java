package org.example.library.service;

import org.example.library.dto.AlbumsDto;
import org.example.library.dto.PlaylistDto;
import org.example.library.dto.TrackStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlaylistService {
      PlaylistDto createPlaylist(PlaylistDto playlistDto, MultipartFile imagePlaylist, Long userId);
      PlaylistDto updatePlaylist(Long PlaylistID, PlaylistDto playlistDto, MultipartFile imagePlaylist, Long userId);
      List<PlaylistDto> getplaylistByUserId(Long userId);
      PlaylistDto getPlaylistById(Long PlaylistID);
      void deletePLaylist(Long playlistID);
      List<PlaylistDto> searchPlaylist(String keyword);
      public List<PlaylistDto> findAll();
      public PlaylistDto findByPlaylistId(Long playlistId);
      public List<PlaylistDto> getAllPlaylist();
}
