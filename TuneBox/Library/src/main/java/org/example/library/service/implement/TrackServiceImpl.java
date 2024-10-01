package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.dto.CommentDto;
import org.example.library.dto.TrackDto;
import org.example.library.mapper.TrackMapper;
import org.example.library.mapper.CommentMapper;
import org.example.library.model.Comment;
import org.example.library.model.Like;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.CommentRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.TrackService;
import org.example.library.utils.ImageUploadTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrackServiceImpl implements TrackService {

    @Autowired
    public TrackRepository trackRepository;

    private final ImageUploadTrack imageUploadTrack;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public TrackDto creatTrack(TrackDto trackDto, MultipartFile image) {
        try {
            Track track = TrackMapper.maptoTrack(trackDto);

            if (image == null){
                track.setTrackImage(null);
            } else {
                imageUploadTrack.uploadFile(image);
                track.setTrackImage(Base64.getEncoder().encodeToString(image.getBytes()));
            }
            track.setStatus(true);
            Track saveTrack = trackRepository.save(track);
            return TrackMapper.maptoTrackDto(saveTrack);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TrackDto updateTrack(Long id, TrackDto trackDto, MultipartFile image) {
        try {
            Track track = trackRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Track not found")
            );
            if (image.getBytes().length > 0) {
                if (imageUploadTrack.checkExist(image)) {
                    track.setTrackImage(track.getTrackImage());
                } else {
                    imageUploadTrack.uploadFile(image);
                    track.setTrackImage(Base64.getEncoder().encodeToString(image.getBytes()));
                }
            }

            track.setName(trackDto.getName());
            track.setStatus(true);
            Track saveTrack = trackRepository.save(track);
            return TrackMapper.maptoTrackDto(saveTrack);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void deleteTrack(Long id) {
        Track track = trackRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Track not found")
        );
        track.setStatus(false);
        trackRepository.save(track);
    }

    @Override
    public void likeTrack(Long id, Long userId) {
        Track track = trackRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Track not found")
        );

        boolean alreadyLiked = track.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(userId));

        if (!alreadyLiked) {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setTrack(track);
            track.getLikes().add(newLike);
            trackRepository.save(track);
        }
    }

    @Override
    public CommentDto commentOnTrack(Long id, Long userId, String comment) {
        Track track = trackRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Track not found")
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        Comment newComment = new Comment();
        newComment.setContent(comment);
        newComment.setCreationDate(LocalDate.now());
        newComment.setTrack(track);
        newComment.setUser(user);
        commentRepository.save(newComment);

        // Chuyển đổi Comment sang CommentDto trước khi trả về
        return CommentMapper.maptoCommentDto(newComment);
    }

    @Override
    public TrackDto getTrackByID(Long id) {
        Track track = trackRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Track not found")
        );
        return TrackMapper.maptoTrackDto(track);
    }

    @Override
    public List<TrackDto> getAllTrack(Long userId) {
        List<Track> tracks = trackRepository.findByCreator_Id(userId);
        return tracks.stream().map(TrackMapper::maptoTrackDto).collect(Collectors.toList());
    }




}
