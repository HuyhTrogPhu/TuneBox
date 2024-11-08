package org.example.library.service;

import org.example.library.dto.AlbumsDto;
import org.example.library.dto.PlaylistDto;
import org.example.library.model.Playlist;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface PlaylistService {
      PlaylistDto createPlaylist(PlaylistDto playlistDto, MultipartFile imagePlaylist, Long userId);
      PlaylistDto updatePlaylist(Long playlistID, PlaylistDto playlistDto, MultipartFile imagePlaylist, Long userId, List<Long> trackIds);
      List<PlaylistDto> getplaylistByUserId(Long userId);
      PlaylistDto getPlaylistById(Long PlaylistID);
      void deletePLaylist(Long playlistID);
      List<PlaylistDto> searchPlaylist(String keyword);
      List<PlaylistDto> getAllPlaylist();
    public PlaylistDto findByPlaylistId(Long playlistId);
      public List<PlaylistDto> findAll();
    Map<LocalDate, Long> countUsersByDateRange(LocalDate startDate, LocalDate endDate);
    Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth);
    Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate);
    List<Playlist> getPlaylistsByDateRange(LocalDate startDate, LocalDate endDate);


      public PlaylistDto removeTrackFromPlaylist(Long playlistId, Long trackId);

}
