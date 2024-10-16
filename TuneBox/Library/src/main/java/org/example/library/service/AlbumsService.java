package org.example.library.service;

import org.example.library.dto.AlbumsDto;
import org.example.library.dto.TrackDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumsService {
    AlbumsDto createAlbums(AlbumsDto albumsDto, MultipartFile imageAlbums, Long userId, Long genreId, Long albumstyleId);
  //  AlbumsDto getAlbumsById(Long id);
  //  List<AlbumsDto> getAllAlbums();
    AlbumsDto updateAlbums(Long albumsId,AlbumsDto albumsDto, MultipartFile imageAlbums, Long userId, Long genreId, Long albumstyleId );
    void deleteAlbums(Long id);
    List<AlbumsDto> getAlbumsByUserId(Long userId);

   // List<TrackDto> getTracksByAlbumId(Long albumId);
   // List<AlbumsDto> searchAlbums(String keyword);
   // AlbumsDto getAlbumsByLabelId(Long labelId);
  //  AlbumsDto getAlbumsByLikeId(Long likeId);
}
