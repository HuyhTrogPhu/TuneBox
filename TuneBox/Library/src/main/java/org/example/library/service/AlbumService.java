package org.example.library.service;

import org.example.library.dto.AlbumsDto;
import org.example.library.model.Albums;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface AlbumService {
    public AlbumsDto getbyUserId(Long UserId);
    public List<AlbumsDto> getAll();
    public AlbumsDto findByAlbumsByID(Long id);
    public List<AlbumsDto> getAllReported();
    Map<LocalDate, Long> countUsersByDateRange(LocalDate startDate, LocalDate endDate);
    Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth);
    Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate);
    List<Albums> getAlbumsByDateRange(LocalDate startDate, LocalDate endDate);
}