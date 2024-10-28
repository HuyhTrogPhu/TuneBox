package org.example.library.service;

import org.example.library.dto.PlaylistDto;

import java.util.List;

public interface PlayListService {
    public List<PlaylistDto> findAll();
    public PlaylistDto findByPlaylistId(Long playlistId);
}
