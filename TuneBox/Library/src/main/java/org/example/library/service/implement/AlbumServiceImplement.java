package org.example.library.service.implement;

import jakarta.persistence.EntityNotFoundException;
import org.example.library.dto.AlbumsDto;
import org.example.library.mapper.AlbumMapper;
import org.example.library.model.Albums;
import org.example.library.repository.AlbumsRepository;
import org.example.library.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return AlbumMapper.mapToDTO(albums);
    }
}
