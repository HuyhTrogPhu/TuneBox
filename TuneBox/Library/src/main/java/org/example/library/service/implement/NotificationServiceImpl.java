package org.example.library.service.implement;

import org.example.library.dto.NotificationDTO;
import org.example.library.model.Follow;
import org.example.library.model.Notification;
import org.example.library.mapper.NotificationMapper;
import org.example.library.model.Post;
import org.example.library.model.User;
import org.example.library.repository.FollowRepository;
import org.example.library.repository.NotificationRepository;
import org.example.library.repository.PostRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendNotificationToFollowers(NotificationDTO notificationDTO, Long targetUserId) {
        // Lấy danh sách những người theo dõi của user
        List<Follow> followers = followRepository.findByFollowedId(targetUserId);

        // Gửi thông báo đến từng người theo dõi
        for (Follow follow : followers) {
            Long followerId = follow.getFollowed().getId();  // Chỉnh sửa phần này

            NotificationDTO followerNotification = new NotificationDTO();
            followerNotification.setMessage(notificationDTO.getMessage());
            followerNotification.setUserId(followerId);
            followerNotification.setCreatedAt(LocalDateTime.now());

            // Lưu thông báo vào cơ sở dữ liệu cho từng follower
            Notification notification = notificationMapper.toEntity(followerNotification);
            notificationRepository.save(notification);

            // Gửi qua WebSocket đến đích riêng của từng user
            messagingTemplate.convertAndSendToUser(followerId.toString(), "/queue/notifications", followerNotification);
        }
    }
    // Trong NotificationServiceImpl
    @Override
    public void notifyFollowersOfNewPost(Long postId, Long userId) {
        // Lấy thông tin bài đăng từ postId
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String avatarUrl = post.getUser().getUserInformation().getAvatar();
        String userName = post.getUser().getUserInformation().getName();

        // Lấy danh sách những người theo dõi của người dùng đã đăng bài
        List<Follow> followers = followRepository.findByFollowedId(userId);

        // Tạo thông báo cho từng người theo dõi
        for (Follow follow : followers) {
            Long followerId = follow.getFollower().getId();

            NotificationDTO notification = new NotificationDTO();
            notification.setPostId(postId);
            notification.setMessage(userName + " đã đăng bài mới."); // Hiển thị tên người dùng
            notification.setUserId(followerId);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setAvatarUrl(avatarUrl);  // Thêm avatar vào thông báo

            // Lưu thông báo vào cơ sở dữ liệu
            Notification notificationEntity = notificationMapper.toEntity(notification);
            notificationRepository.save(notificationEntity);

            // Gửi thông báo qua WebSocket
            messagingTemplate.convertAndSendToUser(followerId.toString(), "/queue/notifications", notification);
        }
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        // Lấy danh sách thông báo theo userId
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setRead(true); // Đánh dấu là đã đọc
            notificationRepository.save(notification); // Lưu cập nhật vào cơ sở dữ liệu
        }
    }

    // Phương thức gửi thông báo khi có người thích bài viết
    @Override
    public void sendLikeNotification(User liker, Post post) {
        Long postOwnerId = post.getUser().getId();
        String postOwnerName = post.getUser().getUserInformation().getName();
        String avatarUrl = liker.getUserInformation().getAvatar();

        // Tạo nội dung thông báo
        NotificationDTO notification = new NotificationDTO();
        notification.setPostId(post.getId());
        notification.setMessage(liker.getUserInformation().getName() + " đã thích bài viết của bạn.");
        notification.setUserId(postOwnerId);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setAvatarUrl(avatarUrl);

        // Lưu thông báo vào cơ sở dữ liệu
        Notification notificationEntity = notificationMapper.toEntity(notification);
        notificationRepository.save(notificationEntity);

        // Gửi thông báo qua WebSocket
        messagingTemplate.convertAndSendToUser(postOwnerId.toString(), "/queue/notifications", notification);
    }
    @Override
    public void sendNotificationcomment(Long userId, String message, Long postId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            // Tạo thông báo mới
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUserId(userId);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setPostId(postId);
            notification.setRead(false); // Mặc định chưa đọc

            // Lưu thông báo vào cơ sở dữ liệu
            notificationRepository.save(notification);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    @Override
    public void deleteNotification(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            notificationRepository.delete(notificationOpt.get()); // Xóa thông báo
        } else {
            throw new IllegalArgumentException("Notification not found");
        }
    }
    @Override
    public void deleteAllReadNotifications(Long userId) {
        List<Notification> readNotifications = notificationRepository.findByUserIdAndRead(userId, true);
        notificationRepository.deleteAll(readNotifications);
    }

    @Override
    public void createNotificationForUser(User user, String message, String type) {
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setMessage(message);
        notification.setType(type);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);  // Thêm trạng thái chưa đọc

        // Lưu vào database
        notificationRepository.save(notification);

        // Gửi realtime notification qua WebSocket
        NotificationDTO notificationDTO = notificationMapper.toDTO(notification);
        messagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/notifications",
                notificationDTO
        );
    }



}
