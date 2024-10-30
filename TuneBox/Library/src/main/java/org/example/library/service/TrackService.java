package org.example.library.service;

import org.example.library.dto.TrackDto;
import org.example.library.dto.TrackStatus;
import org.example.library.model.Track;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {

        public TrackDto createTrack(TrackDto trackDto,MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

        List<TrackDto> getAllTracks();

        TrackDto getTrackById(Long trackId);

        public TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

        public void deleteTrack(Long trackId);

        List<TrackDto> getTracksByUserId(Long userId);

    // get track theo genreId
    List<TrackDto> getTracksByGenreId(Long genreId);

    List<TrackDto> searchTracks (String keywords);
    public TrackStatus getTrackCountCommentandLike(Long Id);
    public List<TrackDto> getAll();

}
