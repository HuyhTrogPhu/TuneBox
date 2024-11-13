package org.example.library.service.implement;

import org.example.library.dto.FollowedUserDto;
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
        follow.setCreatedAt(LocalDateTime.now());

        followRepository.save(follow); // Lưu vào cơ sở dữ liệu
    }

    @Override
    public void unfollowUser(Long followerId, Long followedId) {
        System.out.println("Unfollowing: " + followerId + " is unfollowing " + followedId);
        followRepository.deleteByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followedId) {
        // Kiểm tra và chỉ trả về kết quả một lần
        boolean isFollowing = followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
        System.out.println("Is " + followerId + " following " + followedId + ": " + isFollowing);
        return isFollowing;
    }


    @Override
    public int countFollowers(Long userId) {
        int followersCount = followRepository.countByFollowedId(userId);
        System.out.println("Followers count for user " + userId + ": " + followersCount);
        return followersCount;
    }

    @Override
    public int countFollowing(Long userId) {
        int followingCount = followRepository.countByFollowerId(userId);
        System.out.println("Following count for user " + userId + ": " + followingCount);
        return followingCount;
    }
    @Override
    public List<FollowedUserDto> getFollowers(Long userId) {
        List<Follow> followers = followRepository.findByFollowedId(userId);

        // Lấy danh sách các user đang theo dõi userId
        return followers.stream()
                .map(follow -> follow.getFollower()) // Lấy User từ Follow
                .map(user -> {
                    // Kiểm tra xem người dùng đang theo dõi hay không
                    boolean isFollowing = followRepository.existsByFollowerIdAndFollowedId(user.getId(), userId);
                    return new FollowedUserDto(user.getId(), user.getUserName(), user.getEmail(), user.getUserInformation().getAvatar(), isFollowing);
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<FollowedUserDto> getFollowing(Long userId) {
        List<Follow> followings = followRepository.findByFollowerId(userId);

        // Lấy danh sách các user mà follower đang theo dõi
        return followings.stream()
                .map(follow -> follow.getFollowed()) // Lấy User từ Follow
                .map(user -> {
                    // Kiểm tra xem người dùng đang theo dõi hay không
                    boolean isFollowing = followRepository.existsByFollowerIdAndFollowedId(userId, user.getId());
                    return new FollowedUserDto(user.getId(), user.getUserName(), user.getEmail(), user.getUserInformation().getAvatar(), isFollowing);
                })
                .collect(Collectors.toList());
    }


}