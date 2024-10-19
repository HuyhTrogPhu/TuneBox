package org.example.customer.Controller;

import org.example.library.dto.AlbumStyleDto;
import org.example.library.dto.AlbumsDto;
import org.example.library.dto.GenreDto;
import org.example.library.dto.TrackDto;
import org.example.library.model.AlbumStyle;
import org.example.library.model.Genre;
import org.example.library.model.User;
import org.example.library.repository.AlbumStyleRepository;
import org.example.library.service.AlbumStyleService;
import org.example.library.service.AlbumsService;
import org.example.library.service.GenreService;
import org.example.library.service.TrackService;
import org.example.library.service.implement.AlbumStyleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/albums")
public class AlbumsController {

    @Autowired
    private AlbumsService albumsService;

    @Autowired
    private GenreService genreService;
    @Autowired
    private AlbumStyleServiceImpl albumStyleServiceImpl;
    @Autowired
    private AlbumStyleRepository albumStyleRepository;

    @Autowired
    private AlbumStyleService albumStyleService;

    //creat Track
    @PostMapping
    public ResponseEntity<AlbumsDto> createAlbums(@RequestParam("title") String title, @RequestParam("albumImage") MultipartFile albumImage,
                                                  @RequestParam("description") String description,
                                                  @RequestParam("status") boolean status,
                                                  @RequestParam("report") boolean report,
                                                  @RequestParam("genre") Long genreId, @RequestParam("user") Long userId, @RequestParam("albumStyle") Long albumStyleId,
                                                  @RequestParam(value = "trackIds", required = false) List<Long> trackIds) {
        Set<Long> trackIdSet = new HashSet<>(trackIds);

        try {
            AlbumsDto albumsDto = new AlbumsDto();
            albumsDto.setTitle(title);
            albumsDto.setDescription(description);
            albumsDto.setStatus(status);
            albumsDto.setReport(report);
            albumsDto.setCreateDate(LocalDate.now());
            albumsDto.setTracks(trackIdSet);

            AlbumsDto createAlbums = albumsService.createAlbums(albumsDto,albumImage, genreId, userId, albumStyleId);
            return new ResponseEntity<>(createAlbums, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create album: " + e.getMessage(), e);

        }
    }

    @PutMapping("/{albumsId}")
    public ResponseEntity<AlbumsDto> updateAlbums(@PathVariable Long albumsId,
                                                  @RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "albumImage", required = false) MultipartFile albumImage,
                                                  @RequestParam(value = "description", required = false) String description,
                                                  @RequestParam(value = "status", required = false) boolean status,
                                                  @RequestParam(value = "report", required = false) boolean report,
                                                  @RequestParam(value = "genreId", required = false)  Long genreId,
                                                  @RequestParam(value = "userId", required = false) Long userId,
                                                  @RequestParam(value = "albumstyleId", required = false) Long albumstyleId,
                                                  @RequestParam(value = "trackIds", required = false) Set<Long> trackIds) {

        try {
           // Tạo đối tượng AlbumsDto từ các tham số được truyền vào
            AlbumsDto albumsDto = new AlbumsDto();
            albumsDto.setTitle(title);
            albumsDto.setDescription(description);
            albumsDto.setStatus(status);
            albumsDto.setReport(report);
            albumsDto.setCreateDate(LocalDate.now());
            albumsDto.setTracks(trackIds);

            // Gọi service để cập nhật album
            AlbumsDto updatedAlbum = albumsService.updateAlbums(albumsId, albumsDto, albumImage, userId, genreId, albumstyleId);

            // Trả về phản hồi thành công với album đã cập nhật
            return ResponseEntity.ok(updatedAlbum);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @DeleteMapping("/{albumsId}")
    public ResponseEntity<Void> deleteAlbums(@PathVariable Long albumsId) {
        albumsService.deleteAlbums(albumsId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //get album theo id track
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlbumsDto>> getAlbumsByUserId(@PathVariable Long userId) {
        List<AlbumsDto> albums = albumsService.getAlbumsByUserId(userId);
        if (albums.isEmpty()) {
            System.out.println("No albums found for userId: " + userId);
        } else {
            System.out.println("Found " + albums.size() + " albums for userId: " + userId);
        }

        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/getAllAlbumStyle")
    public ResponseEntity<List<AlbumStyleDto>> getAllAlbumStyle() {
        List<AlbumStyleDto> albumStyleDto = albumStyleService.findAll();
        return ResponseEntity.ok(albumStyleDto);
    }


}
