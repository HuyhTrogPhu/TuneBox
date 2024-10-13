package org.example.library.service.implement;

import org.example.library.dto.TrackDto;
import org.example.library.mapper.TrackMapper;
import org.example.library.model.Genre;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.GenreRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.TrackService;
import org.example.library.utils.ImageUploadTrack;
import org.example.library.utils.Mp3UploadTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUploadTrack imageUploadTrack;

    @Autowired
    private Mp3UploadTrack mp3UploadTrack;


    @Override
    public TrackDto createTrack(TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId) {

        try {
            Genre genre = genreRepository.findById(genreId).orElseThrow(
                    () -> new RuntimeException("Genre not found")
            );

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            Track track = TrackMapper.mapperTrack(trackDto);

            if (imageTrack != null && !imageTrack.isEmpty()) {
                if (!imageUploadTrack.checkExist(imageTrack)) {
                    boolean isUploaded = imageUploadTrack.uploadFile(imageTrack);
                    if (isUploaded) {
                        track.setTrackImage(imageTrack.getOriginalFilename());
                    } else {
                        throw new RuntimeException("Failed to upload track image");
                    }
                } else {
                    track.setTrackImage(imageTrack.getOriginalFilename());
                }
            }

            if (trackFile != null && !trackFile.isEmpty()) {
                if (!mp3UploadTrack.checkExist(trackFile)) {
                    boolean isUploaded = mp3UploadTrack.uploadFile(trackFile);
                    if (isUploaded) {
                        track.setTrackFile(trackFile.getBytes());
                    } else {
                        throw new RuntimeException("Failed to upload track file");
                    }
                } else {
                    track.setTrackFile(trackFile.getBytes());
                }
            }

            track.setCreator(user);
            track.setGenre(genre);
            track.setCreateDate(LocalDate.now());
            track.setStatus(true);
            track.setReport(false);
            track.setAlbums(null);

            trackRepository.save(track);

            return TrackMapper.mapperTrackDto(track);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public TrackDto getAllTracks() {
        return null;
    }

    @Override
    public TrackDto getTrackById(Long trackId) {
        return null;
    }

    @Override
    public TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId) {
        return null;
    }

    @Override
    public void deleteTrack(Long trackId) {

    }
}
