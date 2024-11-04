package org.example.library.service.implement;

import jakarta.persistence.EntityNotFoundException;
import org.example.library.dto.AlbumsDto;
import org.example.library.dto.PlaylistDto;
import org.example.library.mapper.AlbumMapper;

import org.example.library.model.Albums;
import org.example.library.repository.AlbumsRepository;
import org.example.library.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImplement implements AlbumService {
    @Autowired
    AlbumsRepository albumsRepository;

    @Override
    public AlbumsDto getbyUserId(Long UserId){

        List<Albums> albumsList = albumsRepository.findByCreatorId(UserId);
        if (albumsList.isEmpty()) {
            throw new EntityNotFoundException("No albums found for user ID: " + UserId);
        }
        Albums albums = albumsList.get(0);
        return AlbumMapper.mapperAlbumsDto(albums);
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
}
