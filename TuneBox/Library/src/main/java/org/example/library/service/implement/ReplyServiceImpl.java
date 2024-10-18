    package org.example.library.service.implement;

    import jakarta.persistence.EntityNotFoundException;
    import org.example.library.dto.ReplyDto;
    import org.example.library.mapper.ReplyMapper;
    import org.example.library.model.Comment;
    import org.example.library.model.Reply;
    import org.example.library.model.User;
    import org.example.library.repository.CommentRepository;
    import org.example.library.repository.ReplyRepository;
    import org.example.library.repository.UserRepository;
    import org.example.library.service.ReplyService;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class ReplyServiceImpl implements ReplyService {

        private final ReplyRepository replyRepository;
        private final CommentRepository commentRepository;
        private final UserRepository userRepository; // Thêm UserRepository
        private final ReplyMapper replyMapper;

        public ReplyServiceImpl(ReplyRepository replyRepository,
                                CommentRepository commentRepository,
                                UserRepository userRepository, // Thêm vào đây
                                ReplyMapper replyMapper) {
            this.replyRepository = replyRepository;
            this.commentRepository = commentRepository;
            this.userRepository = userRepository; // Khởi tạo
            this.replyMapper = replyMapper;
        }

        @Override
        public ReplyDto addReply(Long commentId, Long userId, ReplyDto replyDto, Long parentReplyId) {
            // Tìm comment cha bằng commentId
            Comment parentComment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            // Tìm user bằng userId
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Tạo mới một reply
            Reply reply = new Reply();
            reply.setContent(replyDto.getContent());
            reply.setCreationDate(LocalDateTime.now());
            reply.setUser(user); // Gán user cho reply
            reply.setParentComment(parentComment); // Gán comment cha cho reply

            // Không cần gán userNickname

            // Nếu parentReplyId không null, tìm reply cha và gán
            if (parentReplyId != null) {
                Reply parentReply = replyRepository.findById(parentReplyId)
                        .orElseThrow(() -> new RuntimeException("Parent reply not found"));
                reply.setParentReply(parentReply);
            }

            // Lưu reply vào cơ sở dữ liệu
            Reply savedReply = replyRepository.save(reply);

            // Chuyển đổi thành ReplyDto để trả về
            return replyMapper.toDto(savedReply);
        }

        @Override
        public ReplyDto addReplyToReply(Long parentReplyId, Long userId, ReplyDto replyDto, Long commentId) {
            Reply parentReply = replyRepository.findById(parentReplyId)
                    .orElseThrow(() -> new EntityNotFoundException("Reply not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Reply newReply = replyMapper.toEntity(replyDto, user, commentId); // Truyền commentId vào đây

            newReply.setParentReply(parentReply); // Thiết lập reply cha

            replyRepository.save(newReply);

            parentReply.getReplies().add(newReply);
            replyRepository.save(parentReply); // Cập nhật reply gốc

            return replyMapper.toDto(newReply); // Chuyển đổi sang DTO và trả về
        }




        @Override
        public List<ReplyDto> getRepliesByComment(Long commentId) {
            // Tìm comment cha
            Comment parentComment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            // Sử dụng đối tượng comment để lấy các reply
            List<Reply> replies = replyRepository.findByParentComment(parentComment);
            return replies.stream()
                    .map(replyMapper::toDto)
                    .collect(Collectors.toList());
        }

        public ReplyDto updateReply(Long replyId, Long userId, ReplyDto replyDto) {
            // Tìm reply bằng replyId
            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new RuntimeException("Reply not found"));

            // Kiểm tra quyền chỉnh sửa (có thể kiểm tra userId)
            if (!reply.getUser().getId().equals(userId)) {
                throw new RuntimeException("User not authorized to update this reply");
            }

            // Cập nhật nội dung của reply
            reply.setContent(replyDto.getContent());

            // Lưu lại reply đã được cập nhật
            Reply updatedReply = replyRepository.save(reply);

            // Chuyển đổi thành ReplyDto để trả về
            return replyMapper.toDto(updatedReply);
        }


        @Override
        public void deleteReply(Long replyId, Long userId) {
            // Tìm reply bằng replyId
            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new RuntimeException("Reply not found"));

            // Kiểm tra quyền xóa (tùy vào logic của bạn, có thể kiểm tra userId)
            if (!reply.getUser().getId().equals(userId)) {
                throw new RuntimeException("User not authorized to delete this reply");
            }

            // Xóa reply
            replyRepository.delete(reply);
        }

    }
