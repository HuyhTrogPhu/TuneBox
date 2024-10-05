package org.example.library.service.implement;

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

import java.util.List;
import java.util.Optional;
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
    public ReplyDto addReply(Long commentId, Long userId, ReplyDto replyDto) {
        // Tìm comment cha
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Tìm user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo reply từ DTO
        Reply reply = replyMapper.toEntity(replyDto, user, parentComment);
        replyRepository.save(reply);

        return replyMapper.toDto(reply); // Trả về DTO của reply đã được thêm
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

    @Override
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }
}
