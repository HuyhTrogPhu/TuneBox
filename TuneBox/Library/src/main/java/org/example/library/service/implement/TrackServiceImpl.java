package org.example.library.service.implement;

import org.example.library.dto.TrackDto;
import org.example.library.mapper.TrackMapper;
import org.example.library.model.Genre;
import org.example.library.model.Track;
import org.example.library.repository.GenreRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;
    private final GenreRepository genreRepository; // Thêm repository này

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository, TrackMapper trackMapper, GenreRepository genreRepository) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
        this.genreRepository = genreRepository; // Khởi tạo repository
    }

    @Override
    public TrackDto createTrack(TrackDto trackDto) {
        // Chuyển từ TrackDto sang Track entity
        Track track = trackMapper.toEntity(trackDto);

        // Lấy Genre từ database dựa trên genreId từ TrackDto
        Genre genre = genreRepository.findById(trackDto.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found")); // Xử lý nếu genre không tồn tại

        // Gán genre vào track
        track.setGenre(genre);

        // Lưu track vào database
        Track savedTrack = trackRepository.save(track);

        // Chuyển từ Track entity sang TrackDto để trả về
        return trackMapper.toDto(savedTrack);
    }
}
