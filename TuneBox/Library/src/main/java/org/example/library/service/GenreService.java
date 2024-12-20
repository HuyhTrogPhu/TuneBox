package org.example.library.service;



import org.example.library.dto.GenreDto;
import org.example.library.dto.GenreUserDto;
import org.example.library.model.Genre;
import org.example.library.model.Talent;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();

    List<Genre> findAllGenre();

    List<GenreUserDto> findNameGenre();
}
