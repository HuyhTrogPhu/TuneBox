package org.example.library.mapper;


import org.example.library.dto.TrackDto;
import org.example.library.model.Track;

public class TrackMapper {

    public static TrackDto maptoTrackDto(Track track) {
        return new TrackDto(track.getId(), track.getName(), track.getTrackImage(), track.getDescription(), track.isStatus(), track.getCreateDate(), track.isReport(), track.getReportDate(), track.getGenre(), track.getCreator(), track.getAlbums(), track.getPlaylists());
    }

    public static Track maptoTrack(TrackDto trackDto) {
        return new Track(trackDto.getId(), trackDto.getName(), trackDto.getTrackImage(), trackDto.getDescription(), trackDto.isStatus(), trackDto.getCreateDate(), trackDto.isReport(), trackDto.getReportDate(), trackDto.getGenre(), trackDto.getCreator(), trackDto.getAlbums(), trackDto.getPlaylists());
    }

}
