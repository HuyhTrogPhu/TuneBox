package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import org.example.library.dto.PLayListDetailSocialAdminDto;
import org.example.library.dto.PlaylistDto;
import org.example.library.dto.*;
import org.example.library.mapper.AlbumMapper;
import org.example.library.mapper.PlaylistMapper;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
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
    private LikeRepository likeRepository;

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
    public PlaylistDto updatePlaylist(Long PlaylistID, PlaylistDto playlistDto, MultipartFile imagePlaylist, Long userId, List<Long> trackIds) {
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

            // Giữ lại track hiện có
            Set<Track> currentTracks = editPlaylist.getTracks();

            // Thêm track mới vào danh sách
            if (trackIds != null) {
                for (Long trackId : trackIds) {
                    Track track = trackRepository.findById(trackId).orElseThrow(
                            () -> new RuntimeException("Track not found with ID: " + trackId)
                    );
                    currentTracks.add(track);
                }
            }

            // Cập nhật playlist với các thông tin mới
            editPlaylist.setTracks(currentTracks);


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
                () -> new RuntimeException("Playlist not found")
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
//fix hereeeee
    @Override
    public PLayListDetailSocialAdminDto findByPlaylistId(Long playlistID) {
        Playlist playlist = playlistRepository.findById(playlistID).orElseThrow(
                () -> new RuntimeException("Album not found")
        );
        List<Track> listTracks = trackRepository.findAllByPlaylistsId(playlist.getId());
        long countLike =likeRepository.countByplaylistId(playlist.getId());
        List<TrackDtoSocialAdmin> trackDtos = listTracks.stream()
                .map(track -> new TrackDtoSocialAdmin(
                        track.getId(),
                        track.getName(),
                        track.getCreateDate(),
                        track.isReport(),
                        track.getReportDate(),
                        track.getCreator().getUserName(),
                        track.getLikes().size()
                ))
                .collect(Collectors.toList());

        PLayListDetailSocialAdminDto playlistDto = new PLayListDetailSocialAdminDto(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getCreateDate(),
                trackDtos,
                playlist.getDescription(),
                countLike,
                playlist.getImagePlaylist()
        );

        return playlistDto;
    }

    public Map<LocalDate, Long> countUsersByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Long count = playlistRepository.countByCreateDate(currentDate);
            userCountMap.put(currentDate, count);
            currentDate = currentDate.plusDays(1);
        }
        return userCountMap;
    }

    public Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        LocalDate currentWeekStart = startDate.with(DayOfWeek.MONDAY);

        while (!currentWeekStart.isAfter(endDate)) {
            LocalDate currentWeekEnd = currentWeekStart.with(DayOfWeek.SUNDAY);
            Long count = playlistRepository.countByCreateDateBetween(currentWeekStart, currentWeekEnd);
            userCountMap.put(currentWeekStart, count);
            currentWeekStart = currentWeekStart.plusWeeks(1);
        }
        return userCountMap;
    }

    public Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth) {
        Map<YearMonth, Long> userCountMap = new HashMap<>();
        YearMonth currentMonth = startMonth;

        while (!currentMonth.isAfter(endMonth)) {
            LocalDate monthStart = currentMonth.atDay(1);
            LocalDate monthEnd = currentMonth.atEndOfMonth();
            Long count = playlistRepository.countByCreateDateBetween(monthStart, monthEnd);
            userCountMap.put(currentMonth, count);
            currentMonth = currentMonth.plusMonths(1);
        }
        return userCountMap;
    }

    public List<PLayListDetailSocialAdminDto> getPlaylistsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Playlist> playlists = playlistRepository.findAllByCreateDateBetween(startDate, endDate);
        List<PLayListDetailSocialAdminDto> playlistDtos = new ArrayList<>();

        for (Playlist playlist : playlists) {
            List<Track> listTracks = trackRepository.findAllByPlaylistsId(playlist.getId());
            List<TrackDtoSocialAdmin> trackDtos = listTracks.stream()
                    .map(track -> new TrackDtoSocialAdmin(
                            track.getId(),
                            track.getName(),
                            track.getCreateDate(),
                            track.isReport(),
                            track.getReportDate(),
                            track.getCreator().getUserName(),
                            track.getLikes().size()
                    ))
                    .collect(Collectors.toList());
            long countLike =likeRepository.countByplaylistId(playlist.getId());

            PLayListDetailSocialAdminDto playlistDto = new PLayListDetailSocialAdminDto(
                    playlist.getId(),
                    playlist.getTitle(),
                    playlist.getCreateDate(),
                    trackDtos,
                    playlist.getDescription(),
                    countLike,
                    playlist.getImagePlaylist()
            );
            playlistDtos.add(playlistDto);
        }

        return playlistDtos;
    }


    @Override
    public PlaylistDto removeTrackFromPlaylist(Long playlistId, Long trackId) {
        try {
            // Tìm playlist theo playlistId
            Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(
                    () -> new RuntimeException("Playlist not found")
            );

            // Tìm track theo trackId
            Track track = trackRepository.findById(trackId).orElseThrow(
                    () -> new RuntimeException("Track not found with ID: " + trackId)
            );

            // Xóa track khỏi playlist
            if (playlist.getTracks().contains(track)) {
                playlist.getTracks().remove(track);
                track.getPlaylists().remove(playlist); // Xóa playlist khỏi tập hợp playlists của track
                playlistRepository.save(playlist); // Lưu cập nhật vào cơ sở dữ liệu
            } else {
                throw new RuntimeException("Track not found in the playlist");
            }

            return PlaylistMapper.mapperPlaylistDto(playlist);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PlaylistDto> getbyUserId(Long UserId){

        List<Playlist> playList = playlistRepository.findByCreatorId(UserId);
        if (playList.isEmpty()) {
            throw new EntityNotFoundException("No playList found for user ID: " + UserId);
        }
        return playList.stream()
                .map(PlaylistMapper::mapperPlaylistDto)
                .collect(Collectors.toList());
    }



}
