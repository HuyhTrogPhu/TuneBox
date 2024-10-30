package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.dto.GenreDto;
import org.example.library.dto.GenreUserDto;
import org.example.library.model.Genre;
import org.example.library.model.Talent;
import org.example.library.repository.GenreRepository;

import org.example.library.service.GenreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {


    @Autowired
    private GenreRepository genreRepository;


    @Override
    public List<GenreDto> findAll() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName())) // Giả sử GenreDto có id và name
                .collect(Collectors.toList());
    }

    @Override
    public List<GenreUserDto> findNameGenre() {
        return genreRepository.findNameGenre();
    }
}
