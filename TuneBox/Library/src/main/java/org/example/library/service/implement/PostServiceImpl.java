    package org.example.library.service.implement;

    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpSession;
    import org.example.library.dto.PostDto;
    import org.example.library.mapper.PostMapper;
    import org.example.library.model.Post;
    import org.example.library.model.PostImage;
    import org.example.library.model.User;
    import org.example.library.repository.PostRepository;
    import org.example.library.service.PostService;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;
    import java.util.stream.Collectors;

    @Service
    public class PostServiceImpl implements PostService {

        private final PostRepository postRepository;

        public PostServiceImpl(PostRepository postRepository) {
            this.postRepository = postRepository;
        }

        @Override
        public PostDto savePost(PostDto postDto, MultipartFile[] images, HttpServletRequest request) throws IOException {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                throw new RuntimeException("User not logged in");
            }

            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                throw new RuntimeException("User ID not found in session");
            }

            postDto.setUserId(userId);
            User user = new User();
            user.setId(userId);

            Post post = PostMapper.toEntity(postDto);
            post.setUser(user);
            post.setCreatedAt(LocalDateTime.now());

            // Xử lý nội dung
            if (postDto.getContent() != null && !postDto.getContent().isEmpty()) {
                post.setContent(postDto.getContent());
            } else {
                post.setContent(""); // Có thể để rỗng nếu không có nội dung
            }

            // Xử lý hình ảnh
            if (images != null && images.length > 0) {
                Set<PostImage> postImages = new HashSet<>();
                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        PostImage postImage = new PostImage();
                        postImage.setPost(post);
                        postImage.setPostImage(image.getBytes());
                        postImages.add(postImage);
                    }
                }
                post.setImages(postImages);
            }

            // Lưu vào database
            Post savedPost = postRepository.save(post);
            return PostMapper.toDto(savedPost);
        }


        @Override
        public PostDto getPostById(Long id) {
            Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
            return PostMapper.toDto(post);
        }

        @Override
        public List<PostDto> getAllPosts() {
            List<Post> posts = postRepository.findAll(); // Lấy tất cả các bài viết từ repository
            return posts.stream()
                    .map(PostMapper::toDto) // Chuyển đổi thành PostDto
                    .collect(Collectors.toList());
        }

        @Override
        public List<PostDto> getPostsByUserId(Long userId) {
            List<Post> posts = postRepository.findByUserId(userId);  // Lấy tất cả các bài đăng của userId
            return posts.stream()
                    .map(PostMapper::toDto)  // Chuyển đổi từ Post entity sang PostDto
                    .collect(Collectors.toList());
        }

        @Override
        public PostDto updatePost(PostDto postDto, MultipartFile[] images, HttpServletRequest request) throws IOException {
            // Lấy session hiện tại
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                throw new RuntimeException("User not logged in");
            }

            // Lấy ID và tên người dùng từ session
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                throw new RuntimeException("User ID not found in session");
            }

            // Kiểm tra xem bài viết có tồn tại không
            Post post = postRepository.findById(postDto.getId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            // Kiểm tra quyền sở hữu bài viết
            if (!post.getUser().getId().equals(userId)) {
                throw new RuntimeException("User does not have permission to update this post");
            }

            // Cập nhật nội dung bài viết
            if (postDto.getContent() != null && !postDto.getContent().isEmpty()) {
                post.setContent(postDto.getContent());
            }

            // Cập nhật hình ảnh nếu có
            if (images != null && images.length > 0) {
                Set<PostImage> postImages = new HashSet<>();
                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        PostImage postImage = new PostImage();
                        postImage.setPost(post);  // Thiết lập quan hệ với Post
                        postImage.setPostImage(image.getBytes()); // Lưu hình ảnh dưới dạng byte[]
                        postImages.add(postImage);
                    }
                }
                post.setImages(postImages);
            }

            // Lưu bài viết đã cập nhật vào database
            Post updatedPost = postRepository.save(post);

            // Chuyển Post entity thành PostDto và trả về
            return PostMapper.toDto(updatedPost);
        }

        @Override
        public void deletePost(Long id) {
            // Kiểm tra xem bài viết có tồn tại không
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            // Xóa bài viết khỏi cơ sở dữ liệu
            postRepository.delete(post);
        }

    }
