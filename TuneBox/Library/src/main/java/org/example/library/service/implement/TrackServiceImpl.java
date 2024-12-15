package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.library.dto.TrackDto;
import org.example.library.dto.TrackDtoSocialAdmin;
import org.example.library.dto.TrackStatus;
import org.example.library.dto.UserDto;
import org.example.library.mapper.TrackMapper;
import org.example.library.model.Genre;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.*;
import org.example.library.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Map;


@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository  likeRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private Cloudinary cloudinary;


    @Override
    public TrackDto createTrack(TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId) {
        try {
            // Lấy thể loại từ cơ sở dữ liệu
            Genre genre = genreRepository.findById(genreId).orElseThrow(
                    () -> new RuntimeException("Genre not found")
            );

            // Lấy người dùng từ cơ sở dữ liệu
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            // Ánh xạ từ TrackDto sang Track
            Track track = TrackMapper.mapperTrack(trackDto);

            // Xử lý tải lên hình ảnh
//            if (imageTrack != null && !imageTrack.isEmpty()) {
//                Map<String, Object> imageResult = cloudinary.uploader().upload(imageTrack.getBytes(), ObjectUtils.emptyMap());
//                String imageUrl = (String) imageResult.get("url"); // Lấy URL của hình ảnh
//                track.setTrackImage(imageUrl); // Lưu URL hình ảnh vào track
//            }
            // Xử lý tải lên hình ảnh
            CompletableFuture<String> imageUploadFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    if (imageTrack != null && !imageTrack.isEmpty()) {
                        Map<String, Object> imageResult = cloudinary.uploader().upload(imageTrack.getBytes(), ObjectUtils.emptyMap());
                        return (String) imageResult.get("url"); // Lấy URL của hình ảnh
                    }
                    return null; // Nếu không có hình ảnh
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image creat: " + e.getMessage(), e);
                }
            });

//            // Xử lý tải lên tệp âm thanh
//            if (trackFile != null && !trackFile.isEmpty()) {
//                Map<String, Object> audioResult = cloudinary.uploader().upload(trackFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
//                String audioUrl = (String) audioResult.get("url"); // Lấy URL của tệp âm thanh
//                track.setTrackFile(audioUrl); // Lưu URL tệp âm thanh vào track
//            }

            // Xử lý tải lên tệp âm thanh
            CompletableFuture<String> audioUploadFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    if (trackFile != null && !trackFile.isEmpty()) {
                        Map<String, Object> audioResult = cloudinary.uploader().upload(trackFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                        return (String) audioResult.get("url"); // Lấy URL của tệp âm thanh
                    }
                    return null; // Nếu không có tệp âm thanh
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload audio: " + e.getMessage(), e);
                }
            });

            // Chờ tất cả các tác vụ tải lên hoàn thành
            String imageUrl = imageUploadFuture.join();
            String audioUrl = audioUploadFuture.join();

            // Lưu URL hình ảnh và tệp âm thanh vào track
            track.setTrackImage(imageUrl);
            track.setTrackFile(audioUrl);

            // Thiết lập thông tin còn lại cho track
            track.setCreator(user);
            track.setGenre(genre);
            track.setCreateDate(LocalDateTime.now());
            track.setStatus(false); // Trạng thái mặc định
            track.setReport(false); // Báo cáo mặc định
            track.setAlbums(null); // Album mặc định

            // Lưu track vào cơ sở dữ liệu
            trackRepository.save(track);

            return TrackMapper.mapperTrackDto(track);
        } catch (Exception e) {
            e.printStackTrace(); // Ghi lại lỗi
            return null; // Trả về null khi có lỗi
        }
    }



    @Override
    public TrackDto updateTrack(Long trackId, TrackDto trackDto, MultipartFile imageTrack, MultipartFile trackFile, Long userId, Long genreId) {
        try {
            // Lấy track từ cơ sở dữ liệu
            Track editTrack = trackRepository.findById(trackId).orElseThrow(
                    () -> new RuntimeException("Track not found")
            );

            // Cập nhật thể loại nếu có
            if (genreId != null) {
                Genre genre = genreRepository.findById(genreId).orElseThrow(
                        () -> new RuntimeException("Genre not found")
                );
                editTrack.setGenre(genre);
            }

            // Cập nhật người tạo nếu có
            if (userId != null) {
                User user = userRepository.findById(userId).orElseThrow(
                        () -> new RuntimeException("User not found")
                );
                editTrack.setCreator(user);
            }

            // Ánh xạ các thuộc tính mới từ trackDto
            editTrack.setName(trackDto.getName());
            editTrack.setDescription(trackDto.getDescription());

            // Xử lý tải hình ảnh mới
//            if (imageTrack != null && !imageTrack.isEmpty()) {
//                // Nếu có hình ảnh cũ, xóa hình ảnh đó
//                if (editTrack.getTrackImage() != null) {
//                    String oldPublicId = extractPublicIdFromUrl(editTrack.getTrackImage());
//                    // Xóa hình ảnh cũ trên Cloudinary
//                    Map<String, Object> params = ObjectUtils.asMap("resource_type", "image");
//                    cloudinary.uploader().destroy(oldPublicId, params);
//                }
//
//                // Tải hình ảnh mới lên Cloudinary
//                Map<String, Object> imageResult = cloudinary.uploader().upload(imageTrack.getBytes(), ObjectUtils.emptyMap());
//                String newImageUrl = (String) imageResult.get("url"); // Lấy URL của hình ảnh mới
//                editTrack.setTrackImage(newImageUrl); // Cập nhật URL hình ảnh mới
//            }

            // Xử lý tải hình ảnh mới
            CompletableFuture<Void> imageUploadFuture = CompletableFuture.runAsync(() -> {
                try {
                    // Nếu có hình ảnh cũ, xóa hình ảnh đó
                    if (editTrack.getTrackImage() != null) {
                        String oldPublicId = extractPublicIdFromUrl(editTrack.getTrackImage());
                        // Xóa hình ảnh cũ trên Cloudinary
                        Map<String, Object> params = ObjectUtils.asMap("resource_type", "image");
                        cloudinary.uploader().destroy(oldPublicId, params);
                    }

                    // Tải hình ảnh mới lên Cloudinary
                    if (imageTrack != null && !imageTrack.isEmpty()) {
                        Map<String, Object> imageResult = cloudinary.uploader().upload(imageTrack.getBytes(), ObjectUtils.emptyMap());
                        String newImageUrl = (String) imageResult.get("url"); // Lấy URL của hình ảnh mới
                        editTrack.setTrackImage(newImageUrl); // Cập nhật URL hình ảnh mới
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
                }
            });

            // Xử lý tải file nhạc mới
//            if (trackFile != null && !trackFile.isEmpty()) {
//                // Nếu có tệp âm thanh cũ, xóa tệp âm thanh đó
//                if (editTrack.getTrackFile() != null) {
//                    String oldPublicId = extractPublicIdFromUrl(editTrack.getTrackFile());
//                    // Xóa tệp âm thanh cũ trên Cloudinary
//                    Map<String, Object> params = ObjectUtils.asMap("resource_type", "raw"); // 'auto' cho âm thanh
//                    cloudinary.uploader().destroy(oldPublicId, params);
//                }
//
//                // Tải tệp âm thanh mới lên Cloudinary
//                Map<String, Object> audioResult = cloudinary.uploader().upload(trackFile.getBytes(), ObjectUtils.asMap("resource_type",  "raw"));
//                String newAudioUrl = (String) audioResult.get("url"); // Lấy URL của tệp âm thanh mới
//                editTrack.setTrackFile(newAudioUrl); // Cập nhật URL tệp âm thanh mới
//            }

            // Xử lý tải file nhạc mới
            CompletableFuture<Void> audioUploadFuture = CompletableFuture.runAsync(() -> {
                try {
                    // Nếu có tệp âm thanh cũ, xóa tệp âm thanh đó
                    if (editTrack.getTrackFile() != null) {
                        String oldPublicId = extractPublicIdFromUrl(editTrack.getTrackFile());
                        // Xóa tệp âm thanh cũ trên Cloudinary
                        Map<String, Object> params = ObjectUtils.asMap("resource_type", "raw"); // 'auto' cho âm thanh
                        cloudinary.uploader().destroy(oldPublicId, params);
                    }

                    // Tải tệp âm thanh mới lên Cloudinary
                    if (trackFile != null && !trackFile.isEmpty()) {
                        Map<String, Object> audioResult = cloudinary.uploader().upload(trackFile.getBytes(), ObjectUtils.asMap("resource_type", "raw"));
                        String newAudioUrl = (String) audioResult.get("url"); // Lấy URL của tệp âm thanh mới
                        editTrack.setTrackFile(newAudioUrl); // Cập nhật URL tệp âm thanh mới
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload audio: " + e.getMessage(), e);
                }
            });

            // Chờ tất cả các tác vụ tải lên hoàn thành
            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(imageUploadFuture, audioUploadFuture);
            combinedFuture.join(); // Chờ cho tất cả các tác vụ hoàn thành

            // Cập nhật ngày tạo và trạng thái
            editTrack.setCreateDate(LocalDateTime.now());
            editTrack.setStatus(false); // Trạng thái mặc định
            editTrack.setReport(false); // Báo cáo mặc định
            editTrack.setAlbums(null); // Album mặc định

            // Lưu track đã cập nhật vào cơ sở dữ liệu
            trackRepository.save(editTrack);

            return TrackMapper.mapperTrackDto(editTrack);
        } catch (Exception e) {
            e.printStackTrace(); // Ghi lại lỗi
            return null; // Trả về null khi có lỗi
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        // Tách publicId từ URL hình ảnh
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1];
        return publicIdWithExtension.split("\\.")[0];  // Lấy publicId trước phần mở rộng ảnh (ví dụ: .jpg)
    }


    @Override
    public void deleteTrack(Long id) {
        Track statusTrack = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        // Chuyển đổi trạng thái của track
        statusTrack.setStatus(true);
        trackRepository.save(statusTrack);
    }

    @Override
    public List<TrackDto> getTracksByUserId(Long userId) {
        return trackRepository.findByCreatorId(userId)
                .stream()
                .map(TrackMapper::mapperTrackDto)
                .collect(Collectors.toList());
    }

    //    @Override
//    public TrackDto getAllTracks() {
//        return null;
//    }

    @Override
    public TrackDto getTrackById(Long trackId) {
        Track track = trackRepository.findById(trackId).orElseThrow(
                () -> new RuntimeException("Track not found")
        );
        return TrackMapper.mapperTrackDto(track);
    }

    @Override
    public TrackDto getTracksById(Long trackId) {
        Track track = trackRepository.findById(trackId).orElseThrow(
                () -> new RuntimeException("Track not found")
        );
        return TrackMapper.mapperTrackDto(track);
    }


    @Override
    public List<TrackDto> getTracksByGenreId(Long genreId) {
        return trackRepository.findByGenreId(genreId)
                .stream()
                .map(TrackMapper::mapperTrackDto)
                .collect(Collectors.toList());
    }

    @Override
    public  List<TrackDto> getAllTracks(Long currentUserId) {
        List<Track> tracks = trackRepository.findTrackdefault(currentUserId);
        return tracks.stream().map(TrackMapper::mapperTrackDto).collect(Collectors.toList());
    }


    @Override
    public List<TrackDto> searchTracks(String keywords) {
        List<Track> tracks = trackRepository.searchByKeywords(keywords);

        // Chuyển đổi danh sách Track thành TrackDto
        return tracks.stream()
                .map(TrackMapper::mapperTrackDto)
                .collect(Collectors.toList());
    }

    @Override
    public TrackStatus getTrackCountCommentandLike(Long Id) {
        TrackStatus trackStatus = new TrackStatus();
        trackStatus.setCommentCount(commentRepository.countByTrackId(Id));
        trackStatus.setLikeCount(likeRepository.countByTrackId(Id));

        return trackStatus;
    }
@Override
    public List<TrackDto> getAll(){
        return trackRepository.findAll()
                .stream()
                .map(TrackMapper::mapperTrackDto)
                .collect(Collectors.toList());
}

@Override
    public List<TrackDto> findByTracksByAlbumId(Long id) {
       return trackRepository.findTracksByAlbumId(id)
               .stream()
               .map(TrackMapper::mapperTrackDto)
               .collect(Collectors.toList());
    }

    @Override
    public List<TrackDtoSocialAdmin> findReportedTrack() {
       List<Track> listTracks = trackRepository.findByReportTrue();
        System.out.println("List RP"+listTracks);
       List<TrackDtoSocialAdmin> ListDto = listTracks.stream()
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
        System.out.println("listDto"+ListDto);
          return ListDto;
    }
    public Map<LocalDate, Long> countTrackByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> trackCountMap = new HashMap<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            // Define the start and end of the day for the current date
            LocalDateTime startOfDay = currentDate.atStartOfDay();
            LocalDateTime endOfDay = currentDate.atTime(LocalTime.MAX);

            // Count tracks within the day's range
            Long count = trackRepository.countByCreateDateBetween(startOfDay, endOfDay);
            trackCountMap.put(currentDate, count);

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }
        return trackCountMap;
    }

    public Map<YearMonth, Long> countUsersByMonthRange(YearMonth startMonth, YearMonth endMonth) {
        Map<YearMonth, Long> userCountMap = new HashMap<>();
        YearMonth currentMonth = startMonth;

        while (!currentMonth.isAfter(endMonth)) {

            LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
            LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59, 59);
            Long count = trackRepository.countByCreateDateBetween(monthStart, monthEnd);
            userCountMap.put(currentMonth, count);
            currentMonth = currentMonth.plusMonths(1);
        }
        return userCountMap;
    }
    public List<Track> getTracksByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();  // 00:00:00 on startDate
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 23:59:59.999 on endDate
        return trackRepository.findAllByCreateDateBetween(startDateTime, endDateTime);
    }
    public Map<LocalDate, Long> countUsersByWeekRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> userCountMap = new HashMap<>();
        LocalDate currentWeekStart = startDate.with(DayOfWeek.MONDAY);

        while (!currentWeekStart.isAfter(endDate)) {

            LocalDateTime weekStart = currentWeekStart.atStartOfDay();
            LocalDateTime weekEnd = currentWeekStart.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

            Long count = trackRepository.countByCreateDateBetween(weekStart, weekEnd);
            userCountMap.put(currentWeekStart, count);
            currentWeekStart = currentWeekStart.plusWeeks(1);
        }
        return userCountMap;
    }
    public Map<String, Long> getTrackCountsByGenreAndDateRange(LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to LocalDateTime
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = trackRepository.countTracksByGenreAndDateRange(startDateTime, endDateTime);
        Map<String, Long> trackCountMap = new HashMap<>();
        for (Object[] result : results) {
            String genre = (String) result[0];
            Long count = (Long) result[1];
            trackCountMap.put(genre, count);
        }
        return trackCountMap;
    }

}