package org.example.library.service.implement;

import org.example.library.model.Friend;
import org.example.library.model.User;
import org.example.library.repository.FriendRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendFriendRequest(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        // Kiểm tra xem đã gửi lời mời hay chưa
        checkExistingRequest(user, friend);

        Friend friendRequest = new Friend();
        friendRequest.setUser(user);
        friendRequest.setFriend(friend);
        friendRequest.setAccepted(false); // Chưa chấp nhận
        friendRequest.setStatus("pending"); // Trạng thái chờ

        friendRepository.save(friendRequest);
    }

    @Override
    public void acceptFriendRequest(Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found with ID: " + requestId));

        friendRequest.setAccepted(true);
        friendRequest.setStatus("accepted");
        friendRepository.save(friendRequest);
    }


    @Override
    public void declineFriendRequest(Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendRequest.setAccepted(false);
        friendRequest.setStatus("declined"); // Cập nhật trạng thái từ chối
        friendRepository.save(friendRequest);
    }

    @Override
    public void unfriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        // Tìm kiếm bản ghi kết bạn, bất kể thứ tự
        Friend friendRecord = findFriendRecord(user, friend);
        if (friendRecord != null) {
            friendRepository.delete(friendRecord);
        } else {
            throw new RuntimeException("Friend relationship not found.");
        }
    }

    @Override
    public void cancelFriendRequest(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        // Tìm lời mời kết bạn giữa hai người dùng
        Friend friendRequest = friendRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new RuntimeException("No pending friend request found."));

        if (!friendRequest.isAccepted() && "pending".equals(friendRequest.getStatus())) {
            // Xóa lời mời kết bạn nếu nó đang ở trạng thái chờ
            friendRepository.delete(friendRequest);
        }
    }

    @Override
    public String checkFriendStatus(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        Optional<Friend> request = friendRepository.findByUserAndFriend(user, friend);
        if (request.isPresent()) {
            Friend friendRequest = request.get();
            if (friendRequest.isAccepted()) {
                return "accepted";
            } else {
                return "pending";
            }
        } else {
            boolean isFriend = friendRepository.existsByUserAndFriend(user, friend);
            return isFriend ? "accepted" : "not_friends";
        }
    }


    // Các phương thức hỗ trợ
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

    private Friend findFriendRecord(User user, User friend) {
        Optional<Friend> friendRecord1 = friendRepository.findByUserAndFriend(user, friend);
        Optional<Friend> friendRecord2 = friendRepository.findByFriendAndUser(friend, user);
        return friendRecord1.orElse(friendRecord2.orElse(null));
    }

    @Override
    public List<Friend> getPendingFriendRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return friendRepository.findByFriendAndStatus(user, "pending");
    }

}
