package org.example.library.service;



import org.example.library.dto.AlbumStyleDto;
import org.example.library.dto.GenreDto;

import java.util.List;

public interface AlbumStyleService {
    List<AlbumStyleDto> findAll();
}
