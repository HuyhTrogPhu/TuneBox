package org.example.library.service;

import org.example.library.dto.TrackDto;
import org.example.library.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface TrackService {

    public TrackDto createTrack(TrackDto trackDto,MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

    public TrackDto getAllTracks();

    public TrackDto getTrackById(Long trackId);

    public TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

    public void deleteTrack(Long trackId);


}
