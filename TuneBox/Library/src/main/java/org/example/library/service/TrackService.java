package org.example.library.service;

import org.example.library.dto.TrackDto;

import java.util.List;

public interface TrackService {

    TrackDto creatTrack(TrackDto trackDto);

    TrackDto updateTrack(Long id, TrackDto trackDto );

    void deleteTrack(Long id);

    void likeTrack(Long id, Long userId);

    void commentOnTrack(Long id, Long userId, String comment);

    TrackDto getTrackByID(Long id);

    List<TrackDto> getAllTrack();
}
