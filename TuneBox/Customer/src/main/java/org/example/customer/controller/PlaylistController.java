package org.example.customer.controller;

import org.example.library.dto.AlbumStyleDto;
import org.example.library.dto.AlbumsDto;
import org.example.library.dto.PlaylistDto;
import org.example.library.dto.TrackDto;
import org.example.library.repository.AlbumStyleRepository;
import org.example.library.service.AlbumStyleService;
import org.example.library.service.AlbumsService;
import org.example.library.service.GenreService;
import org.example.library.service.PlaylistService;
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
@RequestMapping("/customer/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    //creat Track
    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(@RequestParam("title") String title, @RequestParam("imagePlaylist") MultipartFile imagePlaylist,
                                                    @RequestParam("description") String description,
                                                    @RequestParam("status") boolean status,

                                                    @RequestParam("report") boolean report, @RequestParam("user") Long userId, @RequestParam("type") String type,
                                                    @RequestParam(value = "trackIds", required = false) List<Long> trackIds) {
        Set<Long> trackIdSet = new HashSet<>(trackIds);

        try {
            PlaylistDto playlistDto = new PlaylistDto();
            playlistDto.setTitle(title);
            playlistDto.setDescription(description);
            playlistDto.setStatus(status);
            playlistDto.setReport(report);
            playlistDto.setCreateDate(LocalDate.now());
            playlistDto.setTracks(trackIdSet);
            playlistDto.setType(type);

            PlaylistDto createPlaylist = playlistService.createPlaylist(playlistDto, imagePlaylist, userId);
            return new ResponseEntity<>(createPlaylist, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create playlist: " + e.getMessage(), e);

        }
    }

    @PutMapping("/{playlistID}")
    public ResponseEntity<PlaylistDto> updatePlaylist(@PathVariable Long playlistID,
                                                  @RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "imagePlaylist", required = false) MultipartFile imagePlaylist,
                                                  @RequestParam(value = "description", required = false) String description,
                                                  @RequestParam(value = "status", required = false) boolean status,
                                                  @RequestParam(value = "report", required = false) boolean report,
                                                  @RequestParam(value = "userId", required = false) Long userId,
                                                  @RequestParam(value = "type", required = false) String type,
                                                  @RequestParam(value = "trackIds", required = false) List<Long> trackIds) {

        try {
            // Tạo đối tượng AlbumsDto từ các tham số được truyền vào
           PlaylistDto playlistDto = new PlaylistDto();

            playlistDto.setTitle(title);
            playlistDto.setDescription(description);
            playlistDto.setStatus(status);
            playlistDto.setReport(report);
            playlistDto.setCreateDate(LocalDate.now());
            playlistDto.setTracks(new HashSet<>(trackIds)); // Chuyển đổi List thành Set

            // Gọi service để cập nhật album
            PlaylistDto updatePlaylist = playlistService.updatePlaylist(playlistID,playlistDto,imagePlaylist,userId);

            // Trả về phản hồi thành công với album đã cập nhật
            return ResponseEntity.ok(updatePlaylist);
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông báo lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePLaylist(playlistId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //get playlist theo id track
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistDto>> getPlaylistByUserId(@PathVariable Long userId) {
        List<PlaylistDto> playlist = playlistService.getplaylistByUserId(userId);
        if (playlist.isEmpty()) {
            System.out.println("No playlist found for userId: " + userId);
        } else {
            System.out.println("Found " + playlist.size() + " albums for userId: " + userId);
        }

        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    //    get album by id
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistDto> getPlaylistById(@PathVariable Long playlistId) {
        PlaylistDto playlistdto = playlistService.getPlaylistById(playlistId);
        return new ResponseEntity<>(playlistdto, HttpStatus.OK);
    }

    // tìm kiếm Playlist
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<PlaylistDto>> searchPlaylist(@PathVariable String keyword) {
        List<PlaylistDto> result = playlistService.searchPlaylist(keyword);

        // Trả về danh sách
        return ResponseEntity.ok(result);
    }
}
