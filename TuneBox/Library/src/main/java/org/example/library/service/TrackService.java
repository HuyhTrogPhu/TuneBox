package org.example.library.service;

import org.example.library.dto.PostDto;
import org.example.library.dto.TrackDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TrackService {

        public TrackDto createTrack(TrackDto trackDto,MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

//        public TrackDto getAllTracks();

        public TrackDto getTrackById(Long id);

        public TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

        public void deleteTrack(Long trackId);

    List<TrackDto> getTracksByUserId(Long userId);
}
