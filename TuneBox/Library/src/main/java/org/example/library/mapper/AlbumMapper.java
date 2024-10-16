package org.example.library.mapper;

import org.example.library.dto.AlbumsDto;
import org.example.library.model.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

public class AlbumMapper {
    public static AlbumsDto mapToDTO(Albums albums){
        return new AlbumsDto(
        albums.getId(),
        albums.getTitle(),
        albums.getAlbumImage(),
        albums.getDescription(),
        albums.getReleaseDate(),
        albums.getCreateDate(),
        albums.isReport(),
        albums.getReportDate(),
        albums.getStatus(),
        albums.getGenre(),
        albums.getCreator(),
        albums.getTracks(),
        albums.getAlbumStyle()
        );
    }

    public Albums mapToEntity(AlbumsDto albums){
        return new Albums(
                albums.getId(),
                albums.getTitle(),
                albums.getAlbumImage(),
                albums.getDescription(),
                albums.getReleaseDate(),
                albums.getCreateDate(),
                albums.isReport(),
                albums.getReportDate(),
                albums.getStatus(),
                albums.getGenre(),
                albums.getCreator(),
                albums.getTracks(),
                albums.getAlbumStyle()
        );
    }
}
