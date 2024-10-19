package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.library.dto.AlbumsDto;
import org.example.library.dto.TrackDto;
import org.example.library.mapper.AlbumsMapper;
import org.example.library.mapper.TrackMapper;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.AlbumsService;
import org.example.library.service.TrackService;
import org.example.library.utils.ImageUploadAlbums;
import org.example.library.utils.ImageUploadTrack;
import org.example.library.utils.Mp3UploadTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class AlbumsServiceImpl implements AlbumsService {

    @Autowired
    private AlbumsRepository albumsRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AlbumStyleRepository albumStyleRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUploadAlbums imageUploadAlbums;

    @Autowired
    private Cloudinary cloudinary;


    @Override
    public AlbumsDto createAlbums(AlbumsDto albumsDto, MultipartFile imageAlbums, Long userId, Long genreId, Long albumstyleId){

        try {
            // Lấy thể loại từ cơ sở dữ liệu
            Genre genre = genreRepository.findById(genreId).orElseThrow(
                    () -> new RuntimeException("Genre not found")
            );

            // Lấy albums style từ csdl
            AlbumStyle albumStyle = albumStyleRepository.findById(albumstyleId).orElseThrow(
                    () -> new RuntimeException("albumstyle not found")
            );

            // Lấy người dùng từ cơ sở dữ liệu
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            // ánh xa tu AlbumsDto sang Albums
            Albums albums = AlbumsMapper.mapperAlbums(albumsDto);

            // Xử lý tải lên hình ảnh
            CompletableFuture<String> imageUploadFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    if (imageAlbums != null && !imageAlbums.isEmpty()) {
                        Map<String, Object> imageResult = cloudinary.uploader().upload(imageAlbums.getBytes(), ObjectUtils.emptyMap());
                        return (String) imageResult.get("url"); // Lấy URL của hình ảnh
                    }
                    return null; // Nếu không có hình ảnh
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image creat: " + e.getMessage(), e);
                }
            });
            // Chờ tải lên hoàn thành
            String imageUrl = imageUploadFuture.join();
            // Lưu URL hình ảnh và tệp âm thanh vào track
            albums.setAlbumImage(imageUrl);

            // Thêm album vào cơ sở dữ liệu
            albums.setGenre(genre);
            albums.setCreator(user);
            albums.setAlbumStyle(albumStyle);
            albums.setCreateDate(LocalDate.now());
            albums.setStatus(false); // Trạng thái mặc định
            albums.setReport(false); // Báo cáo mặc định

            // Xử lý danh sách track
            if (albumsDto.getTracks() != null) {
                Set<Track> trackSet = new HashSet<>();
                for (Long trackId : albumsDto.getTracks()) {
                    Track track = trackRepository.findById(trackId).orElseThrow(
                            () -> new RuntimeException("Track not found with ID: " + trackId)
                    );
                    track.setAlbums(albums); // Liên kết track với album
                    trackSet.add(track); // Chỉ thêm track đã được quản lý
                }
                albums.setTracks(trackSet); // Thiết lập danh sách track cho album
            }

            albumsRepository.save(albums);

            Albums savedAlbum = albumsRepository.findById(albums.getId()).orElse(null);
            if (savedAlbum != null) {
                System.out.println("Album saved successfully with ID: " + savedAlbum.getId());
            }


            return AlbumsMapper.mapperAlbumsDto(albums);

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    @Override
    public AlbumsDto updateAlbums(Long albumsId, AlbumsDto albumsDto, MultipartFile imageAlbums, Long userId, Long genreId, Long albumstyleId) {
        try {

            Albums editAlbums = albumsRepository.findById(albumsId).orElseThrow(
                    () -> new RuntimeException("Albums not found")
            );

            // Cập nhật thể loại nếu có
            if (genreId != null) {
                Genre genre = genreRepository.findById(genreId).orElseThrow(
                        () -> new RuntimeException("Genre not found")
                );
                editAlbums.setGenre(genre);
            }

            // Cập nhật album style nếu có
            if (albumstyleId != null) {
                AlbumStyle albumStyle = albumStyleRepository.findById(albumstyleId).orElseThrow(
                        () -> new RuntimeException("album style not found")
                );
                editAlbums.setAlbumStyle(albumStyle);
            }

            // Cập nhật người tạo nếu có
            if (userId != null) {
                User user = userRepository.findById(userId).orElseThrow(
                        () -> new RuntimeException("User not found")
                );
                editAlbums.setCreator(user);
            }

            editAlbums.setTitle(albumsDto.getTitle());
            editAlbums.setDescription(albumsDto.getDescription());


            // Xử lý tải hình ảnh mới
            CompletableFuture<Void> imageUploadFuture = CompletableFuture.runAsync(() -> {
                try {
                    // Nếu có hình ảnh cũ, xóa hình ảnh đó
                    if (editAlbums.getAlbumImage() != null) {
                        String oldPublicId = extractPublicIdFromUrl(editAlbums.getAlbumImage());
                        // Xóa hình ảnh cũ trên Cloudinary
                        Map<String, Object> params = ObjectUtils.asMap("resource_type", "image");
                        cloudinary.uploader().destroy(oldPublicId, params);
                    }

                    // Tải hình ảnh mới lên Cloudinary
                    if (imageAlbums != null && !imageAlbums.isEmpty()) {
                        Map<String, Object> imageResult = cloudinary.uploader().upload(imageAlbums.getBytes(), ObjectUtils.emptyMap());
                        String newImageUrl = (String) imageResult.get("url"); // Lấy URL của hình ảnh mới
                        editAlbums.setAlbumImage(newImageUrl); // Cập nhật URL hình ảnh mới
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image albums: " + e.getMessage(), e);
                }
            });

            // Chờ tất cả các tác vụ tải lên hoàn thành
            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(imageUploadFuture);
            combinedFuture.join(); // Chờ cho tất cả các tác vụ hoàn thành

            // Cập nhật thông tin albums vào cơ sở dữ liệu
            editAlbums.setCreateDate(LocalDate.now());
            editAlbums.setStatus(false); // Trạng thái mặc định
            editAlbums.setReport(false); // Báo cáo mặc định

            // Xử lý danh sách track
            if (albumsDto.getTracks() != null) {
                Set<Track> tracks = albumsDto.getTracks().stream()
                        .map(trackId -> {
                            // Tìm kiếm track từ cơ sở dữ liệu
                            Track track = trackRepository.findById(trackId).orElseThrow(
                                    () -> new RuntimeException("Track not found with ID: " + trackId)
                            );
                            return track; // Trả về track đã được quản lý
                        }).collect(Collectors.toSet());
                editAlbums.setTracks(tracks); // Cập nhật danh sách track cho album
            }

            albumsRepository.save(editAlbums);

            return AlbumsMapper.mapperAlbumsDto(editAlbums);

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
    public void deleteAlbums(Long albumsId) {
        Albums statusAlbums = albumsRepository.findById(albumsId).orElseThrow(() -> new RuntimeException("Albums not found"));

        // chuyen doi trang thai album
        statusAlbums.setStatus(true); // 1
        albumsRepository.save(statusAlbums);
    }

    @Override
    public List<AlbumsDto> getAlbumsByUserId(Long userId) {
        return albumsRepository.findByCreatorId(userId) .stream()
                .map(AlbumsMapper::mapperAlbumsDto)
                .collect(Collectors.toList());
    }
}