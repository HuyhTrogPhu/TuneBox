package org.example.library.mapper;

import org.example.library.dto.TrackDto;
import org.example.library.model.Track;
import org.example.library.model.Genre;
import org.example.library.repository.GenreRepository; // Nhập repo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TrackMapper {

    private final GenreRepository genreRepository;

    @Autowired
    public TrackMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public TrackDto toDto(Track track) {
        if (track == null) {
            return null;
        }

        TrackDto dto = new TrackDto();
        dto.setId(track.getId());
        dto.setName(track.getName());
        dto.setTrackImage(track.getTrackImage());
        dto.setDescription(track.getDescription());
        dto.setStatus(track.isStatus());
        dto.setCreateDate(track.getCreateDate());
        dto.setReport(track.isReport());
        dto.setReportDate(track.getReportDate() != null ? LocalDate.from(track.getReportDate().toInstant().atZone(ZoneId.systemDefault())) : null);
        dto.setGenreId(track.getGenre() != null ? track.getGenre().getId() : null); // Kiểm tra Genre
        dto.setUser(track.getUser() != null ? track.getUser().getId() : null); // Kiểm tra User
        dto.setAlbumsId(track.getAlbums() != null ? track.getAlbums().getId() : null); // Kiểm tra Albums

        return dto;
    }

    public Track toEntity(TrackDto dto) {
        if (dto == null) {
            return null;
        }

        Track track = new Track();
        track.setId(dto.getId());
        track.setName(dto.getName());
        track.setTrackImage(dto.getTrackImage());
        track.setDescription(dto.getDescription());
        track.setStatus(dto.isStatus());
        track.setCreateDate(dto.getCreateDate());
        track.setReport(dto.isReport());

        // Ánh xạ Genre từ genreId
        if (dto.getGenreId() != null) {
            Genre genre = new Genre();
            genre.setId(dto.getGenreId());
            track.setGenre(genre);
        } else {
            throw new IllegalArgumentException("Genre must not be null");
        }

        track.setReportDate(dto.getReportDate() != null ? Date.from(dto.getReportDate().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null);

        return track;
    }

}
