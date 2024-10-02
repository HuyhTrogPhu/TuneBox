package org.example.library.mapper;

import org.example.library.dto.TrackDto;
import org.example.library.model.Albums;
import org.example.library.model.Genre;
import org.example.library.model.Track;
import org.example.library.model.User;

public class TrackMapper {

    public static TrackDto maptoTrackDto(Track track) {
        return new TrackDto(
                track.getId(),
                track.getName(),
                track.getTrackImage(),
                track.getDescription(),
                track.isStatus(),
                track.getCreateDate(),
                track.isReport(),
                track.getReportDate(),
                track.getGenre() != null ? track.getGenre().getId() : null,
                track.getCreator() != null ? track.getCreator().getId() : null,
                track.getAlbums() != null ? track.getAlbums().getId() : null,
                track.getPlaylists(),
                track.getComments(),
                track.getLikes()
        );
    }


    public static Track maptoTrack(TrackDto trackDto) {
        Track track = new Track();
        track.setId(trackDto.getId());
        track.setName(trackDto.getName());
        track.setTrackImage(trackDto.getTrackImage());
        track.setDescription(trackDto.getDescription());
        track.setStatus(trackDto.isStatus());
        track.setCreateDate(trackDto.getCreateDate());
        track.setReport(trackDto.isReport());
        track.setReportDate(trackDto.getReportDate());

        if (trackDto.getGenreId() != null) {
            Genre genre = new Genre();
            genre.setId(trackDto.getGenreId());
            track.setGenre(genre);
        }

        if (trackDto.getCreatorId() != null) {
            User user = new User();
            user.setId(trackDto.getCreatorId());
            track.setCreator(user);
        }

        if (trackDto.getAlbumId() != null) {
            Albums album = new Albums();
            album.setId(trackDto.getAlbumId());
            track.setAlbums(album);
        }
        track.setPlaylists(trackDto.getPlaylists());
        track.setComments(trackDto.getComments());
        track.setLikes(trackDto.getLikes());

        return track;
    }
}
