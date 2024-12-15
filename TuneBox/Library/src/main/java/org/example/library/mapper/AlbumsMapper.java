package org.example.library.mapper;

import org.example.library.dto.AlbumsDto;
import org.example.library.dto.TrackDto;
import org.example.library.model.*;

import java.util.Set;
import java.util.stream.Collectors;

public class AlbumsMapper {



    // Phương thức chuyển đổi từ Albums sang AlbumsDto
    // Phương thức chuyển đổi từ Albums sang AlbumsDto
    public static AlbumsDto mapperAlbumsDto(Albums album) {
        return new AlbumsDto(
                album.getId(),
                album.getTitle(),
                album.getAlbumImage(),
                album.getDescription(),
                album.getCreateDate(),
                album.isReport(),
                album.getStatus(),
                album.getGenre() != null ? album.getGenre().getId() : null,  // Genre ID
                album.getCreator() != null ? album.getCreator().getId() : null,  // Creator ID
                album.getAlbumStyle() != null ? album.getAlbumStyle().getId() : null,  // Album Style ID
                album.getTracks() != null ? album.getTracks().stream().map(Track::getId).collect(Collectors.toSet()) : null  // Set of track IDs
        );
    }

    // Phương thức chuyển đổi từ AlbumsDto sang Albums
    public static Albums mapperAlbums(AlbumsDto albumsDto) {
        Albums album = new Albums();
        album.setId(albumsDto.getId());
        album.setTitle(albumsDto.getTitle());
        album.setAlbumImage(albumsDto.getAlbumImage());
        album.setDescription(albumsDto.getDescription());
        album.setCreateDate(albumsDto.getCreateDate());
        album.setReport(albumsDto.isReport());
        album.setStatus(albumsDto.getStatus());

        // Thiết lập Genre từ albumsDto
        if (albumsDto.getGenreId() != null) {
            Genre genre = new Genre();
            genre.setId(albumsDto.getGenreId());
            album.setGenre(genre);  // Assume Genre is already fetched or managed elsewhere
        }

        // Thiết lập Creator từ albumsDto
        if (albumsDto.getCreatorId() != null) {
            User creator = new User();
            creator.setId(albumsDto.getCreatorId());
            album.setCreator(creator);  // Assume Creator is already fetched or managed elsewhere
        }

        // Thiết lập AlbumStyle từ albumsDto
        if (albumsDto.getAlbumStyleId() != null) {
            AlbumStyle albumStyle = new AlbumStyle();
            albumStyle.setId(albumsDto.getAlbumStyleId());
            album.setAlbumStyle(albumStyle);  // Assume AlbumStyle is already fetched or managed elsewhere
        }

        return album;
    }

}
