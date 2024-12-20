package org.example.library.service.implement;

import org.example.library.dto.LikeDto;
import org.example.library.mapper.LikeMapper;
import org.example.library.model.*;
import org.example.library.repository.*;
import org.example.library.service.LikeService;
import org.example.library.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private AlbumsRepository albumsRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository, TrackRepository trackRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
    }
    @Autowired
    NotificationService notificationService;

    @Override
    public LikeDto addLike(Long userId, Long postId, Long trackId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setUser(user);

        // Nếu có postId -> tìm bài viết
        if (postId != null) {
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

            // Kiểm tra xem người dùng đã thích bài viết này chưa
            if (likeRepository.existsByUserAndPost(user, post)) {
                throw new RuntimeException("User already liked this post");
            }
            like.setPost(post);

            // Thêm logic gửi thông báo khi có người thích bài viết
            notificationService.sendLikeNotification(user, post);

        } else if (trackId != null) {
            Track track = trackRepository.findById(trackId).orElseThrow(() -> new RuntimeException("Track not found"));

            // Kiểm tra xem người dùng đã thích track này chưa
            if (likeRepository.existsByUserAndTrack(user, track)) {
                throw new RuntimeException("User already liked this track");
            }
            like.setTrack(track);
        } else {
            throw new RuntimeException("Not found PostId and TrackID");
        }

        like.setCreateDate(LocalDate.now());
        likeRepository.save(like);

        return LikeMapper.toDto(like);
    }

    @Override
    public LikeDto addLikePlaylist(Long userId, Long playlistId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setUser(user);

        // Nếu có postId -> tìm bài viết
        if (playlistId != null) {
            Playlist list = playlistRepository.findById(playlistId).orElseThrow(() -> new RuntimeException("Playlist not found"));

            // Kiểm tra xem người dùng đã thích
            if (likeRepository.existsByUserAndPlaylist(user, list)) {
                throw new RuntimeException("User already liked this playlist");
            }
            System.out.println("list: " + list);
            like.setPlaylist(list);


        } else {
            throw new RuntimeException("Not found Playlist and user");
        }

        like.setCreateDate(LocalDate.now());
        likeRepository.save(like);

        return LikeMapper.toDto(like);
    }

    @Override
    public LikeDto addLikeAlbum(Long userId, Long albumId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setUser(user);

        // Nếu có postId -> tìm bài viết
        if (albumId != null) {
            Albums album = albumsRepository.findById(albumId).orElseThrow(() -> new RuntimeException("Playlist not found"));

            // Kiểm tra xem người dùng đã thích hay chuưa
            if (likeRepository.existsByUserAndAlbums(user, album)) {
                throw new RuntimeException("User already liked this album");
            }
            System.out.println("album: " + album);
            like.setAlbums(album);

        } else {
            throw new RuntimeException("Not found album and user");
        }

        like.setCreateDate(LocalDate.now());
        likeRepository.save(like);

        return LikeMapper.toDto(like);
    }

    @Override
    public void removeLike(Long userId, Long postId, Long trackId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like;

        if (postId != null) {
            // Nếu postId có giá trị, kiểm tra like cho post
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            like = likeRepository.findByUserAndPost(user, post)
                    .orElseThrow(() -> new RuntimeException("Like not found"));
        } else if (trackId != null) {
            // Nếu trackId có giá trị, kiểm tra like cho track
            Track track = trackRepository.findById(trackId)
                    .orElseThrow(() -> new RuntimeException("Track not found"));

            like = likeRepository.findByUserAndTrack(user, track)
                    .orElseThrow(() -> new RuntimeException("Like not found"));
        } else {
            // Nếu cả postId và trackId đều không có
            throw new RuntimeException("not found PostId and TrackID");
        }

        // Xóa like
        likeRepository.delete(like);
    }

    @Override
    public void removeLikePlaylist(Long userId, Long playlistId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like;

        if (playlistId != null) {
            // Nếu postId có giá trị, kiểm tra like cho post
            Playlist list = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new RuntimeException("playlist not found"));

            like = likeRepository.findByUserAndPlaylist(user, list)
                    .orElseThrow(() -> new RuntimeException("Like not found"));
        }  else {
            // Nếu cả postId và trackId đều không có
            throw new RuntimeException("not found playlist");
        }

        // Xóa like
        likeRepository.delete(like);
    }

    @Override
    public void removeLikeAlbum(Long userId, Long albumID) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like;

        if (albumID != null) {
            // Nếu postId có giá trị, kiểm tra like cho post
            Albums album = albumsRepository.findById(albumID)
                    .orElseThrow(() -> new RuntimeException("playlist not found"));

            like = likeRepository.findByUserAndAlbums(user, album)
                    .orElseThrow(() -> new RuntimeException("Like not found"));
        }  else {
            // Nếu cả postId và trackId đều không có
            throw new RuntimeException("not found album");
        }

        // Xóa like
        likeRepository.delete(like);
    }

    public long countLikesByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public long countLikesByTrackId(Long trackId) {
        return likeRepository.countByTrackId(trackId);
    }

    @Override
    public long countLikesByPlaylistId(Long playlistId) {
        return likeRepository.countByplaylistId(playlistId);
    }

    @Override
    public long countLikesByAlbumId(Long albumId) {
        return likeRepository.countByAlbumsId(albumId);
    }

    @Override
    public List<LikeDto> getLikesByPostId(Long postId) {

        List<Like> likes = likeRepository.findByPostId(postId);
        return likes.stream()
                .map(like -> new LikeDto(like.getId(), like.getCreateDate(), like.getUser().getId(), like.getPost().getId(), like.getTrack().getId(), like.getAlbums().getId(),like.getPlaylist().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LikeDto> getLikesByTrackId(Long trackId) {

        List<Like> likes = likeRepository.findByTrackId(trackId);
        return likes.stream()
                .map(LikeMapper::toDtoTrack)
                .collect(Collectors.toList());
    }


    @Override
    public boolean checkUserLike(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        return likeRepository.existsByUserAndPost(user, post);
    }

    @Override
    public boolean checkUserLikeTrack(Long trackId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new RuntimeException("Track not found"));
        return likeRepository.existsByUserAndTrack(user,track);
    }

    @Override
    public boolean checkUserLikePlaylist(Long playlistId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new RuntimeException("Playlist not found"));
        return likeRepository.existsByUserAndPlaylist(user, playlist);
    }

    @Override
    public boolean checkUserLikeAlbum(Long albumId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Albums album = albumsRepository.findById(albumId).orElseThrow(() -> new RuntimeException("Playlist not found"));
        return likeRepository.existsByUserAndAlbums(user, album);
    }

    @Override
    public List<LikeDto> getAllByUserId(Long userId) {
        List<Like> liked = likeRepository.findByUserId(userId);
        return liked.stream().map(LikeMapper::PostAndTrack).collect(Collectors.toList());
    }

    @Override
    public List<LikeDto> getAllAlbumByUserId(Long userId) {
        List<Like> liked = likeRepository.findByUserId(userId);
        return liked.stream().map(LikeMapper::toAlbumDto).collect(Collectors.toList());
    }

    @Override
    public List<LikeDto> getAllPlayListByUserId(Long userId) {
        List<Like> liked = likeRepository.findByUserId(userId);
        return liked.stream().map(LikeMapper::toPlayListDto).collect(Collectors.toList());
    }

    @Override
    public List<Post> getLikedPostsByUser(Long userId) {
        List<Like> likes = likeRepository.findByUserId(userId);
        return likes.stream()
                .map(Like::getPost)
                .collect(Collectors.toList());
    }
}
