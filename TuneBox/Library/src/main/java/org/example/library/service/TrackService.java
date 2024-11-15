package org.example.library.service;

import org.example.library.dto.TrackDto;
import org.example.library.dto.TrackDtoSocialAdmin;
import org.example.library.dto.TrackStatus;
import org.example.library.model.Track;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface TrackService {

         TrackDto createTrack(TrackDto trackDto,MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

        List<TrackDto> getAllTracks();

        TrackDto getTrackById(Long trackId);

         TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId);

         void deleteTrack(Long trackId);

        List<TrackDto> getTracksByUserId(Long userId);

    // get track theo genreId
    List<TrackDto> getTracksByGenreId(Long genreId);

    List<TrackDto> searchTracks (String keywords);

        TrackDto getTracksById(Long trackId);

     TrackStatus getTrackCountCommentandLike(Long Id);
     List<TrackDto> getAll();
     List<TrackDto> findByTracksByAlbumId(Long id);
    public List<TrackDtoSocialAdmin> findReportedTrack();
    public Map<LocalDate, Long> countTrackByDateRange(LocalDate startDate, LocalDate endDate);
    public Map<String, Long> getTrackCountsByGenreAndDateRange(LocalDate startDate, LocalDate endDate);
    Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth);
    Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate);
    List<Track> getTracksByDateRange(LocalDate startDate, LocalDate endDate);
}
