package org.example.library.service.implement;

import org.example.library.dto.LikeDto;
import org.example.library.mapper.LikeMapper;
import org.example.library.model.Like;
import org.example.library.model.Post;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.LikeRepository;
import org.example.library.repository.PostRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.LikeService;
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
    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository, TrackRepository trackRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    public LikeDto addLike(Long userId, Long postId, Long trackId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setUser(user);

        // neu co postId -> tim post
        if (postId != null) {
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
            // Kiểm tra xem người dùng đã thích bài viết này chưa
            if (likeRepository.existsByUserAndPost(user, post)) {
                throw new RuntimeException("User already liked this post");
            }
            like.setPost(post);

        // Neu ko co post, ktra trackId -> tim Track
        } else if (trackId!= null) {
            Track track = trackRepository.findById(trackId).orElseThrow(() -> new RuntimeException("Track not found"));
            // Kiểm tra xem người dùng đã thích track này chưa
            if (likeRepository.existsByUserAndTrack(user, track)) {
                throw new RuntimeException("User already liked this track");
        }
            like.setTrack(track);
        } else {
            throw new RuntimeException("not found PostId and TrackID");
        }

        like.setCreateDate(LocalDate.now());
        // add
        likeRepository.save(like);

        // Sử dụng LikeMapper để chuyển đổi
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
    public List<LikeDto> getLikesByPostId(Long postId) {

        List<Like> likes = likeRepository.findByPostId(postId);
        return likes.stream()
                .map(like -> new LikeDto(like.getId(), like.getCreateDate(), like.getUser().getId(), like.getPost().getId(), like.getTrack().getId()))
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

}
