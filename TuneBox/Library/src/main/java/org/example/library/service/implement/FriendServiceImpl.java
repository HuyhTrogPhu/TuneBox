package org.example.library.service.implement;

import org.example.library.dto.FriendAcceptDto;
import org.example.library.dto.FriendRequestDTO;
import org.example.library.model.Friend;
import org.example.library.model.User;
import org.example.library.repository.FriendRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long sendFriendRequest(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        // Kiểm tra xem đã gửi lời mời hay chưa
        checkExistingRequest(user, friend);

        // Tạo yêu cầu kết bạn mới
        Friend friendRequest = new Friend();
        friendRequest.setUser(user);
        friendRequest.setFriend(friend);
        friendRequest.setAccepted(false);
        friendRequest.setStatus("pending");

        // Lưu yêu cầu kết bạn và trả về ID
        friendRequest = friendRepository.save(friendRequest);
        return friendRequest.getId();
    }

    @Override
    public void acceptFriendRequest(Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found with ID: " + requestId));
        friendRequest.setAccepted(true);
        friendRequest.setStatus("accepted");
        friendRepository.save(friendRequest);

        // Tạo quan hệ bạn bè đối ứng
        User user = friendRequest.getUser();
        User friend = friendRequest.getFriend();
        if (!friendRepository.existsByUserAndFriend(friend, user)) {
            Friend reciprocalFriendRequest = new Friend();
            reciprocalFriendRequest.setUser(friend);
            reciprocalFriendRequest.setFriend(user);
            reciprocalFriendRequest.setAccepted(true);
            reciprocalFriendRequest.setStatus("accepted");
            friendRepository.save(reciprocalFriendRequest);
        }
    }

    @Override
    public List<FriendAcceptDto> getFriends(Long userId) {
        List<FriendAcceptDto> friends = friendRepository.getFriendAccepts(userId);
        return friends;
    }

    @Override
    public String checkFriendStatus(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        // Kiểm tra xem người dùng có gửi yêu cầu kết bạn không
        Optional<Friend> requestSent = friendRepository.findByUserAndFriend(user, friend);
        if (requestSent.isPresent()) {
            return requestSent.get().isAccepted() ? "accepted" : "pending"; // Gửi yêu cầu mà chưa chấp nhận
        }

        // Kiểm tra xem người dùng có nhận yêu cầu kết bạn không
        Optional<Friend> requestReceived = friendRepository.findByFriendAndUser(user, friend);
        if (requestReceived.isPresent()) {
            return requestReceived.get().isAccepted() ? "accepted" : "pending"; // Nhận yêu cầu mà chưa chấp nhận
        }

        // Kiểm tra xem họ có phải là bạn bè không
        boolean isFriend = friendRepository.existsByUserAndFriend(user, friend) ||
                friendRepository.existsByUserAndFriend(friend, user);

        return isFriend ? "accepted" : "not_friends"; // Không có yêu cầu nào
    }

    @Override
    public void declineFriendRequest(Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        // Xóa yêu cầu kết bạn
        friendRepository.delete(friendRequest);
    }

    @Override
    public void unfriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        // Tìm và xóa bản ghi bạn bè
        deleteFriendRecord(user, friend);
        deleteFriendRecord(friend, user);
    }

    @Override
    public void cancelFriendRequest(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        Friend friendRequest = friendRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new RuntimeException("No pending friend request found."));

        if (!friendRequest.isAccepted() && "pending".equals(friendRequest.getStatus())) {
            friendRepository.delete(friendRequest);
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void checkExistingRequest(User user, User friend) {
        Optional<Friend> existingRequest = friendRepository.findByUserAndFriend(user, friend);
        if (existingRequest.isPresent() && !existingRequest.get().isAccepted()) {
            throw new RuntimeException("Friend request already sent.");
        }
    }

    private void deleteFriendRecord(User user, User friend) {
        friendRepository.findByUserAndFriend(user, friend)
                .ifPresent(friendRepository::delete);
    }

    @Override
    public List<FriendRequestDTO> getPendingFriendRequests(Long userId) {
        User user = getUserById(userId);

        // Lấy danh sách lời mời kết bạn đang chờ
        List<Friend> pendingRequests = friendRepository.findByFriendAndStatus(user, "pending");

        // Chuyển đổi sang DTO
        List<FriendRequestDTO> requestDTOs = new ArrayList<>();
        for (Friend request : pendingRequests) {
            FriendRequestDTO dto = new FriendRequestDTO();
            dto.setId(request.getId());
            dto.setRequesterId(request.getUser().getId());
            dto.setRequesterName(request.getUser().getUserName());
            dto.setRequesterUserNickName(request.getUser().getUserInformation().getName());
            dto.setRequesterAvatar(request.getUser().getUserInformation().getAvatar());
            requestDTOs.add(dto);
        }

        return requestDTOs; // Trả về danh sách DTO
    }

    @Override
    public Long countFriends(Long userId) {
        User user = getUserById(userId);

        // Lấy danh sách bạn bè đã được chấp nhận
        List<Friend> friends = friendRepository.findByUserAndStatus(user, "accepted");
        List<Friend> reciprocalFriends = friendRepository.findByFriendAndStatus(user, "accepted");

        // Tạo Set để đảm bảo không có bản sao
        Set<User> friendSet = new HashSet<>();

        for (Friend friend : friends) {
            friendSet.add(friend.getFriend());
        }

        for (Friend reciprocalFriend : reciprocalFriends) {
            friendSet.add(reciprocalFriend.getUser());
        }

        return (long) friendSet.size(); // Trả về số lượng bạn bè duy nhất
    }

}
