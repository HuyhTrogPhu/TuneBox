package org.example.library.service.implement;

import org.example.library.dto.PostDto;
import org.example.library.dto.TrackDto;
import org.example.library.mapper.TrackMapper;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.TrackRepository;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Override
    public TrackDto createTrack(TrackDto trackDto, MultipartFile musicFile, Long userId) throws IOException {

        // Cập nhật TrackDto với thông tin người dùng
        trackDto.setCreatorId(userId); // Lưu userId vào TrackDto
        User user = new User();
        user.setId(userId);

        Track track = TrackMapper.mapToTrack(trackDto); // Chuyển đổi TrackDto thành Track entity
        track.setCreator(user); // Gán User vào track
        track.setCreateDate(LocalDate.now());

        // Xử lý file nhạc
        if (musicFile != null && !musicFile.isEmpty()) {
            // Lưu dữ liệu nhạc dưới dạng byte[]
            track.setMusicFile(musicFile.getBytes()); // Lưu dữ liệu nhạc

            // Tạo tên file nhạc dựa trên title
            String uniqueFileName = trackDto.getName().replaceAll(" ", "_") + "_" + System.currentTimeMillis() + ".mp3";
            track.setName(uniqueFileName); // Lưu tên file nhạc

            // Lưu file nhạc vào thư mục
            Path uploadDir = Paths.get("uploads/music");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path path = uploadDir.resolve(uniqueFileName);
            Files.copy(musicFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        // mô tả
        if (trackDto.getDescription() != null && !trackDto.getDescription().isEmpty()) {
            track.setDescription(trackDto.getDescription());
        } else {
            track.setDescription("");
        }

        // Lưu track vào database
        Track savedTrack = trackRepository.save(track);

        // Chuyển Track entity thành TrackDto và trả về
        return TrackMapper.mapToTrackDto(savedTrack);
    }


    @Override
    public TrackDto getTrackById(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));
        return TrackMapper.mapToTrackDto(track);
    }

    @Override
    public List<TrackDto> getAllTracks() {
        List<Track> tracks = trackRepository.findAll();
        return tracks.stream()
                .map(TrackMapper::mapToTrackDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public TrackDto updateTrack(Long id, TrackDto trackDto, MultipartFile musicFile) {
//        Track existingTrack = trackRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Track not found"));
//
//        existingTrack.setName(trackDto.getName());
//        existingTrack.setDescription(trackDto.getDescription());
//        existingTrack.setStatus(trackDto.isStatus());
//        existingTrack.setCreateDate(trackDto.getCreateDate());
//        existingTrack.setReport(trackDto.isReport());
//        existingTrack.setReportDate(trackDto.getReportDate());
//
//        if (musicFile != null && !musicFile.isEmpty()) {
//            existingTrack.setMusicFile(musicFile.getOriginalFilename()); // Cập nhật tên file
//            // Cập nhật file vào thư mục
//            try {
//                Path path = Paths.get("your/upload/directory/" + musicFile.getOriginalFilename());
//                Files.copy(musicFile.getInputStream(), path);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to store music file", e);
//            }
//        }
//
//        Track updatedTrack = trackRepository.save(existingTrack);
//        return TrackMapper.mapToTrackDto(updatedTrack);
//    }

    @Override
    public void deleteTrack(Long id) {
        Track statusTrack = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        // Chuyển đổi trạng thái của track
        statusTrack.setStatus(false);
        trackRepository.save(statusTrack);
    }

    @Override
    public List<TrackDto> getTracksByUserId(Long userId) {
        return trackRepository.findByCreatorId(userId)
                .stream()
                .map(TrackMapper::mapToTrackDto)
                .collect(Collectors.toList());
    }

}
