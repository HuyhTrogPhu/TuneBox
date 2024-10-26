package org.example.library.mapper;

import org.example.library.dto.GenreDto;
import org.example.library.model.Genre;

import java.util.Set;
import java.util.stream.Collectors;

public class GenreMapper {

    // Hàm chuyển đổi từ Genre sang GenreDto
    public static GenreDto mapToGenreDto(Genre genre) {
        if (genre == null) {
            return null;
        }

        // Lấy ID của từng User, Track, Albums từ các đối tượng liên quan
        Set<Long> userIds = genre.getUser() != null
                ? genre.getUser().stream().map(user -> user.getId()).collect(Collectors.toSet())
                : null;

        Set<Long> trackIds = genre.getTracks() != null
                ? genre.getTracks().stream().map(track -> track.getId()).collect(Collectors.toSet())
                : null;

        Set<Long> albumIds = genre.getAlbums() != null
                ? genre.getAlbums().stream().map(album -> album.getId()).collect(Collectors.toSet())
                : null;

        // Tạo GenreDto và gán các giá trị đã chuyển đổi
        return new GenreDto(
                genre.getId(),
                genre.getName(),
                userIds,
                trackIds,
                albumIds
        );
    }

    // Nếu cần chuyển đổi ngược từ GenreDto sang Genre, bạn có thể thêm hàm mapToGenre() tương tự.
}
