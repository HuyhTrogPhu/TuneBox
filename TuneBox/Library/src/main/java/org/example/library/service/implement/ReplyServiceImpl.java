package org.example.library.service.implement;

import org.example.library.dto.ReplyDto;
import org.example.library.mapper.ReplyMapper;
import org.example.library.model.Comment;
import org.example.library.model.Reply;
import org.example.library.repository.CommentRepository;
import org.example.library.repository.ReplyRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.ReplyService;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
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

        // Tạo mới một reply
        Reply reply = new Reply();
        reply.setContent(replyDto.getContent());
        reply.setCreationDate(LocalDateTime.now());
        reply.setUserId(userId);
        reply.setParentComment(parentComment); // Gán comment cha cho reply

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
    public ReplyDto addReplyToReply(Long parentReplyId, Long userId, ReplyDto replyDto) {
        // Kiểm tra xem reply cha có tồn tại hay không
        Reply parentReply = replyRepository.findById(parentReplyId)
                .orElseThrow(() -> new ResolutionException("Reply not found with ID: " + parentReplyId));

        // Tạo mới reply
        Reply newReply = new Reply();
        newReply.setContent(replyDto.getContent());
        newReply.setCreationDate(LocalDateTime.now()); // Sử dụng LocalDateTime
        newReply.setUserId(userId);
        newReply.setParentReply(parentReply); // Liên kết với reply cha
        newReply.setParentComment(parentReply.getParentComment()); // Liên kết với bình luận cha

        // Lưu reply mới vào cơ sở dữ liệu
        Reply savedReply = replyRepository.save(newReply);

        return replyMapper.toDto(savedReply); // Chuyển đổi thành ReplyDto để trả về
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

    @Override
    public ReplyDto updateReply(Long userId, ReplyDto replyDto) {
        // Tìm reply theo ID
        Reply existingReply = replyRepository.findById(replyDto.getId())
                .orElseThrow(() -> new RuntimeException("Reply not found with ID: " + replyDto.getId()));

        // Kiểm tra xem người dùng có phải là chủ sở hữu của reply không
        if (!existingReply.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to update this reply");
        }


        // Cập nhật nội dung của reply
        existingReply.setContent(replyDto.getContent());
        // Cập nhật thời gian sửa đổi

        // Lưu lại reply đã cập nhật
        Reply updatedReply = replyRepository.save(existingReply);

        // Chuyển đổi thành ReplyDto và trả về
        return replyMapper.toDto(updatedReply);
    }



}
