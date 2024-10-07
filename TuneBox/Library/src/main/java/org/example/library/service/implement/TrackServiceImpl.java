package org.example.library.service.implement;

import org.example.library.dto.TrackDto;
import org.example.library.mapper.TrackMapper;
import org.example.library.model.Track;
import org.example.library.repository.TrackRepository;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository, TrackMapper trackMapper) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
    }

    @Override
    public TrackDto createTrack(TrackDto trackDto) {
        Track track = trackMapper.toEntity(trackDto);
        Track savedTrack = trackRepository.save(track);
        return trackMapper.toDto(savedTrack);
    }
}
