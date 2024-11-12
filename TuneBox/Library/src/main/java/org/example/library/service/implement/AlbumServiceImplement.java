package org.example.library.service.implement;

import jakarta.persistence.EntityNotFoundException;
import org.example.library.dto.AlbumsDto;
import org.example.library.dto.AlbumSocialDto;
import org.example.library.dto.PlaylistDto;
import org.example.library.dto.TrackDtoSocialAdmin;
import org.example.library.mapper.AlbumMapper;

import org.example.library.model.Albums;
import org.example.library.model.Track;
import org.example.library.repository.AlbumsRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImplement implements AlbumService {
    @Autowired
    AlbumsRepository albumsRepository;

    @Autowired
    private TrackRepository trackRepository;


    @Override
    public List<AlbumsDto> getbyUserId(Long UserId){

        List<Albums> albumsList = albumsRepository.findByCreatorId(UserId);
        if (albumsList.isEmpty()) {
            throw new EntityNotFoundException("No albums found for user ID: " + UserId);
        }

        return albumsList.stream().
                map(AlbumMapper::mapperAlbumsDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<AlbumsDto> getAll(){
        return albumsRepository.findAll()
        .stream()
        .map(AlbumMapper::mapperAlbumsDto)
        .collect(Collectors.toList());
    }
    @Override
    public AlbumsDto findByAlbumsByID(Long id) {
        AlbumsDto albumsDto = AlbumMapper.mapperAlbumsDto(albumsRepository.findById(id).get());
        return albumsDto;
    }

    @Override
    public List<AlbumsDto> getAllReported(){
        return albumsRepository.findAllByReportTrue()
                .stream()
                .map(AlbumMapper::mapperAlbumsDto)
                .collect(Collectors.toList());
    }


    public List<AlbumSocialDto> getAlbumsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Albums> listAlbums = albumsRepository.findAllByCreateDateBetween(startDate, endDate);
        List<AlbumSocialDto> listAlbumsDtos = new ArrayList<>();

        // Duyệt qua từng album
        for (Albums album : listAlbums) {
            // Lấy danh sách track của album
            List<Track> listTracks = trackRepository.findAllByAlbumsId(album.getId());

            // Tạo TrackDtoSocialAdmin
            List<TrackDtoSocialAdmin> trackDtos = listTracks.stream()
                    .map(track -> new TrackDtoSocialAdmin(
                            track.getId(),
                            track.getName(),
                            track.getCreateDate(),
                            track.isReport(),
                            track.getReportDate(),
                            track.getCreator().getUserName(),
                            track.getLikes().size()
                    ))
                    .collect(Collectors.toList());

            AlbumSocialDto albumDto = new AlbumSocialDto(
                    album.getId(),
                    album.getTitle(),
                    album.getCreateDate(),
                    album.isReport(),
                    album.getReportDate(),
                    trackDtos,
                    album.getCreator().getUserName(),
                    album.getDescription()

            );

            listAlbumsDtos.add(albumDto);
        }

        return listAlbumsDtos;
    }

    public Map<LocalDate, Long> countUsersByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Long count = albumsRepository.countByCreateDate(currentDate);
            userCountMap.put(currentDate, count);
            currentDate = currentDate.plusDays(1);
        }
        return userCountMap;
    }
    public Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        LocalDate currentWeekStart = startDate.with(DayOfWeek.MONDAY);

        while (!currentWeekStart.isAfter(endDate)) {
            LocalDate currentWeekEnd = currentWeekStart.with(DayOfWeek.SUNDAY);
            Long count = albumsRepository.countByCreateDateBetween(currentWeekStart, currentWeekEnd);
            userCountMap.put(currentWeekStart, count);
            currentWeekStart = currentWeekStart.plusWeeks(1);
        }
        return userCountMap;
    }
    public Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth) {
        Map<YearMonth, Long> userCountMap = new HashMap<>();
        YearMonth currentMonth = startMonth;

        while (!currentMonth.isAfter(endMonth)) {
            LocalDate monthStart = currentMonth.atDay(1);
            LocalDate monthEnd = currentMonth.atEndOfMonth();
            Long count = albumsRepository.countByCreateDateBetween(monthStart, monthEnd);
            userCountMap.put(currentMonth, count);
            currentMonth = currentMonth.plusMonths(1);
        }
        return userCountMap;
    }
}
