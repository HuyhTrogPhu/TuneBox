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

import java.util.List;
import java.util.Optional;
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
    public List<CommentDTO> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO addComment(Long postId, Long userId, CommentDTO commentDTO) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        if (user.isPresent() && post.isPresent()) {
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
}
