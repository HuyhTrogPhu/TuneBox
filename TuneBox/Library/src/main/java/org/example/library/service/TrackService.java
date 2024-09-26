package org.example.library.service;

import org.example.library.dto.TrackDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {

    TrackDto creatTrack(TrackDto trackDto, MultipartFile file);

    TrackDto updateTrack(Long id, TrackDto trackDto, MultipartFile file);

    void deleteTrack(Long id);

    void likeTrack(Long id, Long userId);

    void commentOnTrack(Long id, Long userId, String comment);

    TrackDto getTrackByID(Long id);

    List<TrackDto> getAllTrack();
}
