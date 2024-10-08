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
import java.util.List;
import java.util.stream.Collectors;

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
            track.setStatus(false);
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
    public TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId) {
        try {
            // Lấy idTrack
            Track editTrack = trackRepository.findById(trackId).orElseThrow(
                    () -> new RuntimeException("Trackid not found")
            );

            if (genreId != null) {
                Genre genre = genreRepository.findById(genreId).orElseThrow(
                        () -> new RuntimeException("Genre not found")
                );
                editTrack.setGenre(genre);
            }

            if (userId != null) {
                User user = userRepository.findById(userId).orElseThrow(
                        () -> new RuntimeException(" user not found")
                );
                editTrack.setCreator(user);
            }

            // Ánh xạ các thuộc tính mới từ trackDto
            editTrack.setName(trackDto.getName());
            editTrack.setDescription(trackDto.getDescription());

            // Xử lý tải hình ảnh
            if (imageTrack != null && !imageTrack.isEmpty()) {
                if (!imageUploadTrack.checkExist(imageTrack)) {
                    boolean isUploaded = imageUploadTrack.uploadFile(imageTrack);
                    if (isUploaded) {
                        editTrack.setTrackImage(imageTrack.getOriginalFilename());
                    } else {
                        throw new RuntimeException("Failed to upload track image");
                    }
                } else {
                    editTrack.setTrackImage(imageTrack.getOriginalFilename());
                }
            }

            // Xử lý tải file nhạc
            if (trackFile != null && !trackFile.isEmpty()) {
                if (!mp3UploadTrack.checkExist(trackFile)) {
                    boolean isUploaded = mp3UploadTrack.uploadFile(trackFile);
                    if (isUploaded) {
                        editTrack.setTrackFile(trackFile.getBytes());
                    } else {
                        throw new RuntimeException("Tải file nhạc thất bại");
                    }
                } else {
                    editTrack.setTrackFile(trackFile.getBytes());
                }
            }

            editTrack.setCreateDate(LocalDate.now());
            editTrack.setStatus(false);
            editTrack.setReport(false);
            editTrack.setAlbums(null);

            // Lưu track đã cập nhật
            trackRepository.save(editTrack);

            return TrackMapper.mapperTrackDto(editTrack);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteTrack(Long id) {
        Track statusTrack = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        // Chuyển đổi trạng thái của track
        statusTrack.setStatus(true);
        trackRepository.save(statusTrack);
    }

    @Override
    public List<TrackDto> getTracksByUserId(Long userId) {
        return trackRepository.findByCreatorId(userId)
                .stream()
                .map(TrackMapper::mapperTrackDto)
                .collect(Collectors.toList());
    }

    //    @Override
//    public TrackDto getAllTracks() {
//        return null;
//    }

    @Override
    public TrackDto getTrackById(Long trackId) {
        return null;
    }

}