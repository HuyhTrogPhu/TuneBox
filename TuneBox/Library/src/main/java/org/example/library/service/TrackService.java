package org.example.library.service;

import org.example.library.dto.TrackDto;
import org.example.library.model.Track;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {

        public TrackDto createTrack(TrackDto trackDto,MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

//        public TrackDto getAllTracks();

        TrackDto getTrackById(Long trackId);

        public TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

        public void deleteTrack(Long trackId);

    List<TrackDto> getTracksByUserId(Long userId);
}
