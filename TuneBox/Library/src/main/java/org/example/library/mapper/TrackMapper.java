package org.example.library.mapper;

import org.example.library.dto.TrackDto;
import org.example.library.model.Track;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TrackMapper {

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
        dto.setGenreId(track.getGenre().getId());
        dto.setUser(track.getUser().getId());
        dto.setAlbumsId(track.getAlbums().getId());

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

        track.setReportDate(dto.getReportDate() != null ? Date.from(dto.getReportDate().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null);

        return track;
    }
}
