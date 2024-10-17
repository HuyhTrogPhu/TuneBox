package org.example.library.service.implement;

import org.example.library.dto.FollowDto;
import org.example.library.mapper.FollowMapper;
import org.example.library.model.Follow;
import org.example.library.model.User;
import org.example.library.repository.FollowRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public void followUser(Long followerId, Long followedId) {
        System.out.println("Following: " + followerId + " is following " + followedId);
        // Kiểm tra nếu follower và followed có tồn tại trong DB
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("Followed user not found"));

        // Tạo mới một Follow entity
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        
        followRepository.save(follow); // Lưu vào cơ sở dữ liệu
    }

    @Override
    public void unfollowUser(Long followerId, Long followedId) {
        System.out.println("Unfollowing: " + followerId + " is unfollowing " + followedId);
        followRepository.deleteByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followedId) {
        boolean isFollowing = followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
        System.out.println("Is " + followerId + " following " + followedId + ": " + isFollowing);
        return followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public int countFollowers(Long userId) {
        int followersCount = followRepository.countByFollowedId(userId);
        System.out.println("Followers count for user " + userId + ": " + followersCount);
        return followRepository.countByFollowedId(userId); // Đếm người theo dõi
    }

    @Override
    public int countFollowing(Long userId) {
        int followingCount = followRepository.countByFollowerId(userId);
        System.out.println("Following count for user " + userId + ": " + followingCount);
        return followRepository.countByFollowerId(userId); // Đếm người đang theo dõi
    }

    @Override
    public List<FollowDto> getFollowers(Long userId) {
        List<Follow> followers = followRepository.findByFollowedId(userId);
        return followers.stream()
                .map(followMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowDto> getFollowing(Long userId) {
        List<Follow> followings = followRepository.findByFollowerId(userId);
        return followings.stream()
                .map(followMapper::toDto)
                .collect(Collectors.toList());
    }
}
