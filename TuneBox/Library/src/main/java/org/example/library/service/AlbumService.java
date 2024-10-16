package org.example.library.service;

import org.example.library.dto.AlbumsDto;

public interface AlbumService {
    public AlbumsDto getbyUserId(Long UserId);
}
