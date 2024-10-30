
package org.example.library.mapper;

import org.example.library.dto.TrackDto;
import org.example.library.model.*;

import java.util.Set;
import java.util.stream.Collectors;

public class TrackMapper {

    public static TrackDto mapperTrackDto(Track track) {
        Set<Long> playlist = track.getPlaylists() != null ? track.getPlaylists().stream()
                .map(Playlist::getId).collect(Collectors.toSet()) : null;

        Set<Long> comments = track.getComments() != null ? track.getComments().stream()
                .map(Comment::getId).collect(Collectors.toSet()) : null;

        Set<Long> likes = track.getLikes() != null ? track.getLikes().stream()
                .map(Like::getId).collect(Collectors.toSet()) : null;

        // Lấy ID của album
        Set<Long> albumIds = track.getAlbums() != null ?
                track.getAlbums().stream().map(Albums::getId).collect(Collectors.toSet()) : null;

        return new TrackDto(
                track.getId(),
                track.getName(),
                track.getTrackImage(),
                track.getTrackFile(),
                track.getDescription(),
                track.isStatus(),
                track.getCreateDate(),
                track.isReport(),
                track.getReportDate(),
                track.getGenre() != null ? track.getGenre().getId() : null,
                track.getGenre() != null ? track.getGenre().getName() : null,
                track.getCreator() != null ? track.getCreator().getId() : null,
                track.getCreator() != null ? track.getCreator().getUserName() : null,
                albumIds,  // Chuyển albumIds vào đây
                playlist,
                comments,
                likes
        );
    }


    public static Track mapperTrack(TrackDto trackDto) {
        Track track = new Track();
        track.setId(trackDto.getId());
        track.setName(trackDto.getName());
        track.setTrackImage(trackDto.getImageTrack());
        track.setTrackFile(trackDto.getTrackFile());
        track.setDescription(trackDto.getDescription());
        track.setStatus(trackDto.isStatus()); // Convert Long to boolean
        track.setCreateDate(trackDto.getCreateDate());
        track.setReport(trackDto.isReport());
        track.setReportDate(trackDto.getReportDate());

        // Set the genre, user (creator), and albums using IDs from trackDto
        Genre genre = new Genre();
        genre.setId(trackDto.getGenreId());
        genre.setName(trackDto.getGenreName());
        track.setGenre(genre);  // Assume genre is already fetched or managed elsewhere

        User creator = new User();
        creator.setId(trackDto.getUserId());
        creator.setUserName(trackDto.getUserName());
        track.setCreator(creator);  // Assume user is already fetched or managed elsewhere

        Set<Albums> albums = trackDto.getAlbumIds() != null ?
                trackDto.getAlbumIds().stream().map(id -> {
                    Albums album = new Albums();
                    album.setId(id);
                    return album;
                }).collect(Collectors.toSet()) : null;
        track.setAlbums(albums);

        // Set playlists (if necessary)
        Set<Playlist> playlists = trackDto.getPlaylistIds() != null ?
                trackDto.getPlaylistIds().stream().map(id -> {
                    Playlist playlist = new Playlist();
                    playlist.setId(id);
                    return playlist;
                }).collect(Collectors.toSet()) : null;
        track.setPlaylists(playlists);

        // Set comments (if necessary)
        Set<Comment> comments = trackDto.getComments() != null ?
                trackDto.getComments().stream().map(id -> {
                    Comment comment = new Comment();
                    comment.setId(id);
                    return comment;
                }).collect(Collectors.toSet()) : null;
        track.setComments(comments);

        // Set likes (if necessary)
        Set<Like> likes = trackDto.getLikes() != null ?
                trackDto.getLikes().stream().map(id -> {
                    Like like = new Like();
                    like.setId(id);
                    return like;
                }).collect(Collectors.toSet()) : null;
        track.setLikes(likes);

        return track;
    }


}