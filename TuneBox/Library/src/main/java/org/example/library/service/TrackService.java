package org.example.library.service;

import org.example.library.dto.PostDto;
import org.example.library.dto.TrackDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TrackService {
    TrackDto createTrack(TrackDto trackDto, MultipartFile musicFile, Long userId) throws IOException;
    TrackDto getTrackById(Long id);
    List<TrackDto> getAllTracks();
   // TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile musicFile, Long userId);
    void deleteTrack(Long id);
    List<TrackDto> getTracksByUserId(Long userId);
}
