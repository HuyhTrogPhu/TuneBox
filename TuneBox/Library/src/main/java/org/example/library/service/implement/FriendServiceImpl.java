package org.example.library.service.implement;

import org.example.library.dto.FriendAcceptDto;
import org.example.library.dto.FriendRequestDto;
import org.example.library.model.Friend;
import org.example.library.model.User;
import org.example.library.model_enum.FriendStatus;
import org.example.library.repository.FriendRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        checkExistingRequest(user, friend); // Nếu đã gửi yêu cầu thì ném ra ngoại lệ
        // Tạo yêu cầu kết bạn mới
        Friend friendRequest = new Friend();
        friendRequest.setUser(user);
        friendRequest.setFriend(friend);
        friendRequest.setAccepted(false);
        friendRequest.setStatus(FriendStatus.PENDING_SENT);
        friendRepository.save(friendRequest);
        // Tạo yêu cầu kết bạn đối ứng với trạng thái "PENDING_RECEIVED"
        Friend reciprocalRequest = new Friend();
        reciprocalRequest.setUser(friend);
        reciprocalRequest.setFriend(user);
        reciprocalRequest.setAccepted(false);
        reciprocalRequest.setStatus(FriendStatus.PENDING_RECEIVED);
        friendRepository.save(reciprocalRequest);
        return friendRequest.getId();
    }

    @Override
    public void acceptFriendRequest(Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found with ID: " + requestId));

        friendRequest.setAccepted(true);
        friendRequest.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friendRequest);

        // Cập nhật trạng thái của yêu cầu đối ứng
        Friend reciprocalRequest = friendRepository.findByUserAndFriend(friendRequest.getFriend(), friendRequest.getUser())
                .orElseThrow(() -> new RuntimeException("Reciprocal friend request not found"));

        reciprocalRequest.setAccepted(true);
        reciprocalRequest.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(reciprocalRequest);
    }
    @Override
    public void declineFriendRequest(Long requestId) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendRepository.delete(friendRequest);

        // Xóa yêu cầu đối ứng nếu có
        friendRepository.findByUserAndFriend(friendRequest.getFriend(), friendRequest.getUser())
                .ifPresent(friendRepository::delete);
    }


    @Override
    public List<FriendAcceptDto> getFriends(Long userId) {
        List<FriendAcceptDto> friends = friendRepository.getFriendAccepts(userId);
        return friends;
    }

    @Override
    public Map<String, Object> checkFriendStatus(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        Optional<Friend> requestSent = friendRepository.findByUserAndFriend(user, friend);
        Optional<Friend> requestReceived = friendRepository.findByFriendAndUser(friend, user);

        Map<String, Object> response = new HashMap<>();

        if (requestSent.isPresent() && requestSent.get().getStatus() == FriendStatus.PENDING_SENT) {
            response.put("status", FriendStatus.PENDING_SENT.name());
            response.put("requestId", requestSent.get().getId());
        } else if (requestReceived.isPresent() && requestReceived.get().getStatus() == FriendStatus.PENDING_RECEIVED) {
            response.put("status", FriendStatus.PENDING_RECEIVED.name());
            response.put("requestId", requestReceived.get().getId());
        } else if ((requestSent.isPresent() || requestReceived.isPresent()) && (requestSent.get().isAccepted() || requestReceived.get().isAccepted())) {
            response.put("status", FriendStatus.ACCEPTED.name());
            response.put("requestId", requestSent.isPresent() ? requestSent.get().getId() : requestReceived.get().getId());
        } else {
            response.put("status", "Add Friend");
            response.put("requestId", null);
        }

        System.out.println("Returning " + response + " for userId: " + userId + " and friendId: " + friendId);
        return response;
    }



    public boolean isBlocked(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        // Kiểm tra xem có một bản ghi nào thể hiện rằng user đã chặn friend không
        Optional<Friend> blockRecord = friendRepository.findByUserAndFriend(user, friend);
        if (blockRecord.isPresent()) {
            return blockRecord.get().getStatus() == FriendStatus.BLOCKED; // Trả về true nếu bị chặn
        }

        return false; // Không bị chặn
    }

    @Override
    public List<FriendRequestDto> getFriendRequests(Long userId) {
        List<Friend> friendRequests = friendRepository.findByFriendId(userId);

        return friendRequests.stream().map(friend -> {
            boolean isSender = friend.getUser().getId().equals(userId);
            String senderAvatar = friend.getUser().getUserInformation().getAvatar();
            String senderName = friend.getUser().getUserInformation().getName();

            // Trả về DTO mới
            return new FriendRequestDto(
                    friend.getId(), // requestId
                    isSender ? friend.getUser().getId() : friend.getFriend().getId(), // senderId
                    isSender ? friend.getFriend().getId() : friend.getUser().getId(), // receiverId
                    isSender, // true nếu là người gửi, false nếu là người nhận
                    friend.getStatus().name(), // status
                    senderAvatar, // senderAvatar
                    senderName // senderName
            );
        }).collect(Collectors.toList());
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

        // Kiểm tra và xoá yêu cầu kết bạn từ cả hai phía
        Optional<Friend> pendingRequestSent = friendRepository.findByUserAndFriendAndStatus(user, friend, FriendStatus.PENDING_SENT);
        Optional<Friend> pendingRequestReceived = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.PENDING_RECEIVED);

        if (pendingRequestSent.isEmpty() && pendingRequestReceived.isEmpty()) {
            throw new RuntimeException("No pending friend request found.");
        }

        pendingRequestSent.ifPresent(friendRepository::delete);
        pendingRequestReceived.ifPresent(friendRepository::delete);
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
    public Long countFriends(Long userId) {
        User user = getUserById(userId);

        // Lấy danh sách bạn bè đã được chấp nhận
        List<Friend> friends = friendRepository.findByUserAndStatus(user, FriendStatus.ACCEPTED);
        List<Friend> reciprocalFriends = friendRepository.findByFriendAndStatus(user, FriendStatus.ACCEPTED);

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