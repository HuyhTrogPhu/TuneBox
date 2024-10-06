package org.example.library.mapper;

import org.example.library.dto.TrackDto;
import org.example.library.model.*;

import java.util.stream.Collectors;

public class TrackMapper {

    public static TrackDto mapToTrackDto(Track track) {
        if (track == null) {
            return null; // Xử lý trường hợp đối tượng track là null
        }

        return new TrackDto(
                track.getId(),
                track.getName(),
                track.getTrackImage(),
                track.getMusicFile(), // Thêm musicFile nếu cần
                track.getDescription(),
                track.isStatus(),
                track.getCreateDate(),
                track.isReport(),
                track.getReportDate(),
                track.getGenre() != null ? track.getGenre().getId() : null,
                track.getCreator() != null ? track.getCreator().getId() : null,
                track.getAlbums() != null ? track.getAlbums().getId() : null,
                track.getPlaylists() != null ? track.getPlaylists().stream().map(p -> p.getId()).collect(Collectors.toSet()) : null, // Ánh xạ ID cho playlists
                track.getComments() != null ? track.getComments().stream().map(c -> c.getId()).collect(Collectors.toSet()) : null, // Ánh xạ ID cho comments
                track.getLikes() != null ? track.getLikes().stream().map(l -> l.getId()).collect(Collectors.toSet()) : null // Ánh xạ ID cho likes
        );
    }

    public static Track mapToTrack(TrackDto trackDto) {
        if (trackDto == null) {
            return null; // Xử lý trường hợp đối tượng trackDto là null
        }

        Track track = new Track();
        track.setId(trackDto.getId());
        track.setName(trackDto.getName());
        track.setTrackImage(trackDto.getTrackImage());
        track.setMusicFile(trackDto.getMusicFile()); // Thêm musicFile nếu cần
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

        // Ánh xạ ID cho playlists, comments và likes
        track.setPlaylists(trackDto.getPlaylistsId() != null ?
                trackDto.getPlaylistsId().stream().map(id -> {
                    Playlist playlist = new Playlist();
                    playlist.setId(id);
                    return playlist;
                }).collect(Collectors.toSet()) : null);

        track.setComments(trackDto.getCommentsId() != null ?
                trackDto.getCommentsId().stream().map(id -> {
                    Comment comment = new Comment();
                    comment.setId(id);
                    return comment;
                }).collect(Collectors.toSet()) : null);

        track.setLikes(trackDto.getLikesId() != null ?
                trackDto.getLikesId().stream().map(id -> {
                    Like like = new Like();
                    like.setId(id);
                    return like;
                }).collect(Collectors.toSet()) : null);

        return track;
    }
}
