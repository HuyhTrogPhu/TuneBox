package org.example.library.service;

import org.example.library.dto.AlbumSocialDto;
import org.example.library.dto.AlbumsDto;
import org.example.library.model.Albums;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface AlbumService {
    List<AlbumsDto> getbyUserId(Long UserId);
    List<AlbumSocialDto> getAll();
    AlbumSocialDto findByAlbumsByID(Long id);
    List<AlbumsDto> getAllReported();
    Map<LocalDate, Long> countUsersByDateRange(LocalDate startDate, LocalDate endDate);
    Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth);
    Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate);
    List<AlbumSocialDto> getAlbumsByDateRange(LocalDate startDate, LocalDate endDate);
}
