package org.example.library.service.implement;

import org.example.library.dto.CommentDTO;
import org.example.library.mapper.CommentMapper;
import org.example.library.model.Comment;
import org.example.library.model.Post;
import org.example.library.model.User;
import org.example.library.repository.CommentRepository;
import org.example.library.repository.PostRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

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
            comment = commentRepository.save(comment);
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
        // Lấy tất cả các bình luận cho bài viết
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
                commentDTO.setReplies(replies); // Thêm bình luận trả lời vào bình luận gốc
                topLevelComments.add(commentDTO);
            }
        }

        return topLevelComments; // Trả về danh sách bình luận gốc cùng với bình luận trả lời
    }

}
