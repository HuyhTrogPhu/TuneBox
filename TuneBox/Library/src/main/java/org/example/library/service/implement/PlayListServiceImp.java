package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.library.dto.PlaylistDto;
import org.example.library.mapper.PlaylistMapper;
import org.example.library.model.Playlist;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.AlbumsRepository;
import org.example.library.repository.PlaylistRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Service
public class PlayListServiceImp implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private AlbumsRepository albumsRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public PlaylistDto createPlaylist(PlaylistDto playlistDto, MultipartFile imagePlaylist, Long userId) {
        try {
            // Lấy người dùng từ cơ sở dữ liệu
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            Playlist playlist = PlaylistMapper.mapperPlaylist(playlistDto);
            // Xử lý tải lên hình ảnh
            CompletableFuture<String> imageUploadFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    if (imagePlaylist != null && !imagePlaylist.isEmpty()) {
                        Map<String, Object> imageResult = cloudinary.uploader().upload(imagePlaylist.getBytes(), ObjectUtils.emptyMap());
                        return (String) imageResult.get("url"); // Lấy URL của hình ảnh
                    }
                    return null; // Nếu không có hình ảnh
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload playlist creat: " + e.getMessage(), e);
                }
            });
            // Chờ tải lên hoàn thành
            String imageUrl = imageUploadFuture.join();
            // Lưu URL hình ảnh và tệp âm thanh vào track
            playlist.setImagePlaylist(imageUrl);
            playlist.setCreator(user);
            playlist.setCreateDate(LocalDate.now());
            playlist.setType(playlistDto.getType());
            playlist.setStatus(false);
            playlist.setReport(false);
            playlist.setDescription(playlistDto.getDescription());

            // Xử lý danh sách track
            if (playlistDto.getTracks() != null) {
                for (Long trackId : playlistDto.getTracks()) {
                    Track track = trackRepository.findById(trackId).orElseThrow(
                            () -> new RuntimeException("Track not found with ID: " + trackId)
                    );
                    // Thêm track vào tập hợp tracks của album
                    playlist.getTracks().add(track);

                    // Nếu albums chưa được khởi tạo trong track, khởi tạo nó
                    if (track.getPlaylists() == null) {
                        track.setPlaylists(new HashSet<>());
                    }
                    // Thêm album vào tập hợp albums của track
                    track.getPlaylists().add(playlist);
                }
            }

            playlistRepository.save(playlist);

            return PlaylistMapper.mapperPlaylistDto(playlist);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public PlaylistDto updatePlaylist(Long PlaylistID, PlaylistDto playlistDto, MultipartFile imagePlaylist, Long userId) {
        try {

            Playlist editPlaylist = playlistRepository.findById(PlaylistID).orElseThrow(
                    () -> new RuntimeException("Playlist not found")
            );

            editPlaylist.setTitle((playlistDto.getTitle()));
            editPlaylist.setDescription(playlistDto.getDescription());

            // Xử lý tải hình ảnh mới
            CompletableFuture<Void> imageUploadFuture = CompletableFuture.runAsync(() -> {
                try {
                    // Nếu có hình ảnh cũ, xóa hình ảnh đó
                    if (editPlaylist.getImagePlaylist() != null) {
                        String oldPublicId = extractPublicIdFromUrl(editPlaylist.getImagePlaylist());
                        // Xóa hình ảnh cũ trên Cloudinary
                        Map<String, Object> params = ObjectUtils.asMap("resource_type", "image");
                        cloudinary.uploader().destroy(oldPublicId, params);
                    }

                    // Tải hình ảnh mới lên Cloudinary
                    if (imagePlaylist != null && !imagePlaylist.isEmpty()) {
                        Map<String, Object> imageResult = cloudinary.uploader().upload(imagePlaylist.getBytes(), ObjectUtils.emptyMap());
                        String newImageUrl = (String) imageResult.get("url"); // Lấy URL của hình ảnh mới
                        editPlaylist.setImagePlaylist(newImageUrl); // Cập nhật URL hình ảnh mới
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image Playlist: " + e.getMessage(), e);
                }
            });

            // Chờ tất cả các tác vụ tải lên hoàn thành
            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(imageUploadFuture);
            combinedFuture.join(); // Chờ cho tất cả các tác vụ hoàn thành

            // Cập nhật thông tin albums vào cơ sở dữ liệu
            editPlaylist.setCreateDate(LocalDate.now());
            editPlaylist.setStatus(false); // Trạng thái mặc định
            editPlaylist.setReport(false); // Báo cáo mặc định

            // Xử lý danh sách track
            if (playlistDto.getTracks() != null) {
                Set<Track> tracks = playlistDto.getTracks().stream()
                        .map(trackId -> {
                            // Tìm kiếm track từ cơ sở dữ liệu
                            Track track = trackRepository.findById(trackId).orElseThrow(
                                    () -> new RuntimeException("Track not found with ID: " + trackId)
                            );
                            return track; // Trả về track đã được quản lý
                        }).collect(Collectors.toSet());
                editPlaylist.setTracks(tracks); // Cập nhật danh sách track cho album
            }

            playlistRepository.save(editPlaylist);

            return PlaylistMapper.mapperPlaylistDto(editPlaylist);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //    trích xuất Public ID từ URL của hình ảnh đã được tải lên Cloudinary
    private String extractPublicIdFromUrl(String imageUrl) {
        // Tách publicId từ URL hình ảnh
        String[] parts = imageUrl.split("/"); //sẽ chia URL thành một mảng các phần, mỗi phần được phân tách bởi dấu /.
        String publicIdWithExtension = parts[parts.length - 1]; //lấy phần cuối cùng của mảng
        return publicIdWithExtension.split("\\.")[0];  //  sẽ chia tên tệp bằng dấu chấm (.) Lấy publicId trước phần mở rộng ảnh (ví dụ: .jpg)
    }

    @Override
    public List<PlaylistDto> getplaylistByUserId(Long userId) {
        return playlistRepository.findByCreatorId(userId).stream().map(PlaylistMapper::mapperPlaylistDto).collect(Collectors.toList());
    }


    @Override
    public void deletePLaylist(Long PlaylistID) {
        Playlist statusPlaylist = playlistRepository.findById(PlaylistID).orElseThrow(() -> new RuntimeException("Playlist not found"));

        // chuyen doi trang thai album
        statusPlaylist.setStatus(true); // 1
        playlistRepository.save(statusPlaylist);
    }

    @Override
    public PlaylistDto getPlaylistById(Long playlistID) {
        Playlist playlist = playlistRepository.findById(playlistID).orElseThrow(
                () -> new RuntimeException("Album not found")
        );
        return PlaylistMapper.mapperPlaylistDto(playlist);
    }

    //    search
    @Override
    public List<PlaylistDto> searchPlaylist(String keyword) {
        List<Playlist> playlists = playlistRepository.searchByKeywords(keyword);

        // Chuyển đổi danh sách Track thành TrackDto
        return playlists.stream()
                .map(PlaylistMapper::mapperPlaylistDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistDto> getAllPlaylist() {
        List<Playlist> playlists = playlistRepository.findAll();
        return playlists.stream().map(PlaylistMapper::mapperPlaylistDto).collect(Collectors.toList());
    }


    @Override
    public List<PlaylistDto> findAll() {
        return playlistRepository.findAll()
                .stream()
                .map(PlaylistMapper::mapperPlaylistDto)
                .collect(Collectors.toList());

    }

    @Override
    public PlaylistDto findByPlaylistId(Long playlistId) {
        PlaylistDto playlistDTO =PlaylistMapper.mapperPlaylistDto(playlistRepository.findById(playlistId).get());
    return playlistDTO;
    }

}
