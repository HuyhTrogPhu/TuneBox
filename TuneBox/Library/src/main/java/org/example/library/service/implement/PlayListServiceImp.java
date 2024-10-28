package org.example.library.service.implement;

import org.example.library.dto.PlaylistDto;
import org.example.library.mapper.PlayListMapper;
import org.example.library.model.Playlist;
import org.example.library.repository.PlaylistRepository;
import org.example.library.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class PlayListServiceImp implements PlayListService {
    @Autowired
    private PlaylistRepository playlistRepository;

@Override
    public List<PlaylistDto> findAll() {
        return playlistRepository.findAll()
                .stream()
                .map(PlayListMapper::mapToDto)
                .collect(Collectors.toList());

    }

    @Override
    public PlaylistDto findByPlaylistId(Long playlistId) {
        PlaylistDto playlistDTO =PlayListMapper.mapToDto(playlistRepository.findById(playlistId).get());
    return playlistDTO;
    }

}
