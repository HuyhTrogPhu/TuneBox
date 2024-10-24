package org.example.library.service.implement;

import org.example.library.dto.CommentDTO;
import org.example.library.dto.NotificationDTO;
import org.example.library.mapper.CommentMapper;
import org.example.library.model.Comment;
import org.example.library.model.Post;
import org.example.library.model.Track;
import org.example.library.model.User;
import org.example.library.repository.CommentRepository;
import org.example.library.repository.PostRepository;
import org.example.library.repository.TrackRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.CommentService;
import org.example.library.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final TrackRepository trackRepository;
    private final NotificationService notificationService;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, CommentMapper commentMapper, TrackRepository trackRepository,NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
        this.trackRepository = trackRepository;
        this.notificationService = notificationService;
    }

    // add comment to post
    @Override
    public CommentDTO addComment(Long postId, Long userId, CommentDTO commentDTO) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        if (user.isPresent() && post.isPresent()) {
            // Kiểm tra xem commentDTO có parentId không
            if (commentDTO.getParentId() != null) {
                // Có thể kiểm tra sự tồn tại của bình luận cha nếu cần
                Optional<Comment> parentComment = commentRepository.findById(commentDTO.getParentId());
                if (!parentComment.isPresent()) {
                    throw new IllegalArgumentException("Parent comment not found");
                }
            }

            Comment comment = commentMapper.toEntity(commentDTO, user.get(), post.get());
            comment.setCreationDate(LocalDateTime.now());
            comment = commentRepository.save(comment);

            // Gửi thông báo cho người dùng đã tạo bài viết
            Long postOwnerId = post.get().getUser().getId(); // Lấy ID của người tạo bài viết
            String notificationMessage = user.get().getUserInformation().getName() + " đã bình luận vào bài viết của bạn."; // Nội dung thông báo
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(user.get().getUserInformation().getName());
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
            notificationService.sendNotificationcomment(postOwnerId, notificationMessage, postId); // Gửi thông báo

            return commentMapper.toDto(comment);
        } else {
            throw new IllegalArgumentException("Post or User not found");
        }
    }



    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDTO> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        // Tạo một danh sách bình luận DTO
        List<CommentDTO> commentDTOs = comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        // Tạo một bản đồ để nhóm các bình luận theo parentId
        Map<Long, List<CommentDTO>> repliesMap = new HashMap<>();
        for (CommentDTO commentDTO : commentDTOs) {
            if (commentDTO.getParentId() != null) {
                repliesMap.computeIfAbsent(commentDTO.getParentId(), k -> new ArrayList<>()).add(commentDTO);
            }
        }

        // Tạo một danh sách để lưu các bình luận gốc
        List<CommentDTO> topLevelComments = new ArrayList<>();
        for (CommentDTO commentDTO : commentDTOs) {
            if (commentDTO.getParentId() == null) {
                // Nếu không có parentId, đây là bình luận gốc
                List<CommentDTO> replies = repliesMap.get(commentDTO.getId());
                if (replies != null) {
                    // Đệ quy xử lý các trả lời (replies) theo nhiều cấp
                    commentDTO.setReplies(getNestedReplies(replies, repliesMap)); // Sử dụng phương thức getNestedReplies
                }
                topLevelComments.add(commentDTO);
            }
        }

        return topLevelComments; // Trả về danh sách bình luận gốc với các reply
    }

    // Phương thức hỗ trợ để lấy các replies theo nhiều cấp
    private List<CommentDTO> getNestedReplies(List<CommentDTO> replies, Map<Long, List<CommentDTO>> repliesMap) {
        for (CommentDTO reply : replies) {
            List<CommentDTO> nestedReplies = repliesMap.get(reply.getId());
            if (nestedReplies != null) {
                reply.setReplies(getNestedReplies(nestedReplies, repliesMap)); // Đệ quy để lấy replies của replies
            }
        }
        return replies;
    }


    @Override
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);

        if (existingComment.isPresent()) {
            Comment comment = existingComment.get();

            // Cập nhật nội dung bình luận
            comment.setContent(commentDTO.getContent());
            comment.setEdited(true); // Đánh dấu bình luận là đã chỉnh sửa

            comment = commentRepository.save(comment); // Lưu lại bình luận đã chỉnh sửa
            return commentMapper.toDto(comment); // Trả về bình luận đã chỉnh sửa dưới dạng DTO
        } else {
            throw new IllegalArgumentException("Comment not found");
        }
    }



    // --------------------------------Comment Track --------------------------------
    // add comment to Track
    @Override
    public CommentDTO addCommentTrack(Long trackId, Long userId, CommentDTO commentDTO) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Track> track = trackRepository.findById(trackId);

        //.isPresent() kiểm ttra sự tồn tại cua obj, ton tai tra ve true
        if (user.isPresent() && track.isPresent()) {
            // Kiểm tra xem commentDTO có parentId(cha) không
            if (commentDTO.getParentId() != null) {
                // Có thể kiểm tra sự tồn tại của bình luận cha nếu cần
                Optional<Comment> parentComment = commentRepository.findById(commentDTO.getParentId());
                if (!parentComment.isPresent()) { // parent khong ton tai ->loi
                    throw new IllegalArgumentException("Parent comment not found");
                }
            }

            Comment comment = commentMapper.toEntity_Track(commentDTO, user.get(), track.get());
            comment.setCreationDate(LocalDateTime.now());
            comment = commentRepository.save(comment);
            return commentMapper.toDto_Track(comment);
        } else {
            throw new IllegalArgumentException("Post or User not found");
        }
    }

    // Xoa comment track
    @Override
    public void deleteCommentTrack(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // Chinh sua comment track
    @Override
    public CommentDTO updateCommentTrack(Long commentId, CommentDTO commentDTO) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);

        //kta su ton tai
        if (existingComment.isPresent()) {
            Comment comment = existingComment.get();

            // Cập nhật nội dung bình luận
            comment.setContent(commentDTO.getContent());
            comment.setEdited(true); // Đánh dấu bình luận là đã chỉnh sửa

            comment = commentRepository.save(comment); // Lưu lại bình luận đã chỉnh sửa
            return commentMapper.toDto_Track(comment); // Trả về bình luận đã chỉnh sửa dưới dạng DTO
        } else {
            throw new IllegalArgumentException("Comment not found");
        }
    }

    // Phương thức này dùng để lấy các phản hồi (replies) theo cấu trúc phân cấp
    private List<CommentDTO> getNestedReplies_Track(List<CommentDTO> replies, Map<Long, List<CommentDTO>> repliesMap) {
        // Duyệt qua từng phản hồi trong danh sách replies
        for (CommentDTO reply : replies) {

            // Tìm danh sách các phản hồi con (nếu có) của phản hồi hiện tại từ repliesMap
            List<CommentDTO> nestedReplies = repliesMap.get(reply.getId());

            // Nếu tồn tại phản hồi con, gọi đệ quy để tiếp tục tìm các phản hồi của phản hồi con
            if (nestedReplies != null) {
                // Gán danh sách phản hồi con vào cho phản hồi hiện tại
                reply.setReplies(getNestedReplies_Track(nestedReplies, repliesMap)); // Đệ quy
            }
        }

        // Trả về danh sách các phản hồi đã được phân cấp
        return replies;
    }

    @Override
    public List<CommentDTO> getCommentsByTrack(Long trackId) {
        List<Comment> comments = commentRepository.findByTrackId(trackId);

        // Tạo một danh sách bình luận DTO
        List<CommentDTO> commentDTOs = comments.stream()
                .map(commentMapper::toDto_Track)
                .collect(Collectors.toList());

        // Tạo một bản đồ để nhóm các bình luận theo parentId
        Map<Long, List<CommentDTO>> repliesMap = new HashMap<>();
        for (CommentDTO commentDTO : commentDTOs) {
            if (commentDTO.getParentId() != null) {
                repliesMap.computeIfAbsent(commentDTO.getParentId(), k -> new ArrayList<>()).add(commentDTO);
            }
        }

        // Tạo một danh sách để lưu các bình luận gốc
        List<CommentDTO> topLevelComments = new ArrayList<>();
        for (CommentDTO commentDTO : commentDTOs) {
            if (commentDTO.getParentId() == null) {
                // Nếu không có parentId, đây là bình luận gốc
                List<CommentDTO> replies = repliesMap.get(commentDTO.getId());
                if (replies != null) {
                    // Đệ quy xử lý các trả lời (replies) theo nhiều cấp
                    commentDTO.setReplies(getNestedReplies_Track(replies, repliesMap)); // Sử dụng phương thức getNestedReplies_track
                }
                topLevelComments.add(commentDTO);
            }
        }
        return topLevelComments; // Trả về danh sách bình luận gốc với các reply
    }

}
