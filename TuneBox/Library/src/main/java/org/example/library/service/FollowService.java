package org.example.library.service;

import org.example.library.dto.FollowedUserDto;
import org.example.library.dto.UserDto;


import java.util.List;

public interface FollowService {

    void followUser(Long followerId, Long followedId); // Theo dõi người dùng
    void unfollowUser(Long followerId, Long followedId); // Hủy theo dõi người dùng
    boolean isFollowing(Long followerId, Long followedId); // Kiểm tra xem đã theo dõi chưa
    int countFollowers(Long userId); // Đếm số lượng người theo dõi
    int countFollowing(Long userId); // Đếm số lượng người đang theo dõi
    List<UserDto> getFollowers(Long userId); // Lấy danh sách người theo dõi
    List<UserDto> getFollowing(Long userId); // Lấy danh sách người đang theo dõi
}
