package org.example.library.service.implement;

import org.example.library.dto.LikeDto;
import org.example.library.mapper.LikeMapper;
import org.example.library.model.Like;
import org.example.library.model.Post;
import org.example.library.model.User;
import org.example.library.repository.LikeRepository;
import org.example.library.repository.PostRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LikeDto addLike(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        // Kiểm tra xem người dùng đã thích bài viết này chưa
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new RuntimeException("User already liked this post");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        // Sử dụng LikeMapper để chuyển đổi
        return LikeMapper.toDto(like);
    }


    @Override
    public void removeLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likeRepository.delete(like);
    }

    @Override
    public List<LikeDto> getLikesByPostId(Long postId) {

        List<Like> likes = likeRepository.findByPostId(postId);
        return likes.stream()
                .map(like -> new LikeDto(like.getId(), like.getCreateDate(), like.getUser().getId(), like.getPost().getId()))
                .collect(Collectors.toList());
    }
    @Override
    public boolean checkUserLike(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        return likeRepository.existsByUserAndPost(user, post);
    }

}
