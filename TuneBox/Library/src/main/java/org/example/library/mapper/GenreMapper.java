package org.example.library.mapper;


import org.example.library.dto.GenreDto;
import org.example.library.dto.UserDto;
import org.example.library.model.Genre;
import org.example.library.model.User;

public class GenreMapper {

    public static GenreDto maptoGenreDto(Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName()
        );
    }

    public static Genre maptoGenre(GenreDto genredto) {
        return new Genre(
                genredto.getId(),
                genredto.getName()
        );
    }

}
