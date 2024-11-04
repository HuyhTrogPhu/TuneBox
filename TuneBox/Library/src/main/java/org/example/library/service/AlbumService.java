package org.example.library.service;

import org.example.library.dto.AlbumsDto;

import java.util.List;

public interface AlbumService {
    public AlbumsDto getbyUserId(Long UserId);
    public List<AlbumsDto> getAll();
    public AlbumsDto findByAlbumsByID(Long id);
    public List<AlbumsDto> getAllReported();
}
