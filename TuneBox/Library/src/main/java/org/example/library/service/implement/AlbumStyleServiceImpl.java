package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.dto.AlbumStyleDto;
import org.example.library.dto.GenreDto;
import org.example.library.model.AlbumStyle;
import org.example.library.model.Genre;
import org.example.library.repository.AlbumStyleRepository;
import org.example.library.repository.GenreRepository;
import org.example.library.service.AlbumStyleService;
import org.example.library.service.AlbumsService;
import org.example.library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AlbumStyleServiceImpl implements AlbumStyleService {


    @Autowired
    private AlbumStyleRepository Style;


    @Override
    public List<AlbumStyleDto> findAll() {
        List<AlbumStyle> albumStyles = Style.findAll();
        return albumStyles.stream()
                .map(albumStyle -> new AlbumStyleDto(albumStyle.getId(), albumStyle.getName())) // Giả sử GenreDto có id và name
                .collect(Collectors.toList());
    }
}
