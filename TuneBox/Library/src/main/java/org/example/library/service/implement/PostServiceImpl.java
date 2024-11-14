package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.library.dto.PostDto;
//import org.example.library.dto.PostReportDto;
import org.example.library.dto.PostReactionDto;
import org.example.library.dto.ReportDto;
import org.example.library.dto.UserInfoDto;
import org.example.library.mapper.PostMapper;
//import org.example.library.mapper.PostReportMapper;
import org.example.library.model.*;
//import org.example.library.model.PostReport;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.PostRepository;
import org.example.library.repository.ReportRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final Cloudinary cloudinary;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ReportRepository  reportRepository, Cloudinary cloudinary) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.cloudinary = cloudinary;
    }



    @Override
    public PostDto savePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException {

        // Tìm User từ cơ sở dữ liệu dựa vào userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật PostDto với userId và userName
        postDto.setUserId(user.getId());
        postDto.setUserNickname(user.getUserInformation().getName());

        Post post = PostMapper.toEntity(postDto); // Chuyển đổi PostDto thành Post entity
        post.setUser(user); // Gán User vào bài đăng
        post.setCreatedAt(LocalDateTime.now());

        // Xử lý hình ảnh nếu có
        if (images != null && images.length > 0) {
            Set<PostImage> postImages = new HashSet<>();
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    try {
                        // Upload ảnh lên Cloudinary
                        Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                        String imageUrl = (String) uploadResult.get("url");

                        // Tạo đối tượng PostImage và gán URL từ Cloudinary
                        PostImage postImage = new PostImage();
                        postImage.setPost(post);  // Thiết lập quan hệ với Post
                        postImage.setPostImage(imageUrl); // Lưu URL của hình ảnh
                        postImages.add(postImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Failed to upload image");
                    }
                }
            }
            post.setImages(postImages); // Gán tập hình ảnh vào bài viết
        }

        // Nếu không có nội dung, có thể để trống
        if (postDto.getContent() != null && !postDto.getContent().isEmpty()) {
            post.setContent(postDto.getContent());
        } else {
            post.setContent(""); // Đặt giá trị nội dung là chuỗi rỗng nếu không có
        }

        // Lưu post vào database
        Post savedPost = postRepository.save(post);

        // Chuyển Post entity thành PostDto và trả về
        return PostMapper.toDto(savedPost);
    }


    @Override
    public List<PostDto> getAllPosts(Long currentUserId) {
        // Retrieve all posts excluding those from blocked users or users who have blocked the current user
        List<Post> posts = postRepository.findPostsExcludingBlockedUsers(currentUserId);
        return posts.stream()
                .map(PostMapper::toDto) // Convert to PostDto
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByUserId(Long userId, String currentUsername) {
        List<Post> posts;

        // Lấy thông tin người dùng từ userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Kiểm tra xem người dùng hiện tại có phải là chủ sở hữu của trang cá nhân không
        if (user.getUserName().equals(currentUsername)) {
            // Nếu là chủ sở hữu, lấy tất cả bài viết
            posts = postRepository.findByUserId(userId);
        } else {
            // Nếu không phải, chỉ lấy các bài viết không bị ẩn
            posts = postRepository.findByUserIdAndHidden(userId, false);
        }

        return posts.stream().map(PostMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PostDto updatePost(PostDto postDto, MultipartFile[] images, Long userId) throws IOException {

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
                    try {
                        // Upload ảnh lên Cloudinary
                        Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                        String imageUrl = (String) uploadResult.get("url");

                        // Tạo đối tượng PostImage và gán URL từ Cloudinary
                        PostImage postImage = new PostImage();
                        postImage.setPost(post);  // Thiết lập quan hệ với Post
                        postImage.setPostImage(imageUrl); // Lưu URL của hình ảnh
                        postImages.add(postImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Failed to upload image");
                    }
                }
            }
            post.setImages(postImages); // Gán tập hình ảnh mới vào bài viết
        }

        // Lưu bài viết đã cập nhật vào database
        Post updatedPost = postRepository.save(post);
        updatedPost.setEdited(true); // Đánh dấu bài viết đã chỉnh sửa

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

    @Override
    public void updateReportPost(Post post) {
        List<Report> reports = reportRepository.findByPost(post);

        if (reports.isEmpty()) {
            throw new RuntimeException("No reports found for the post with ID: " + post.getId());
        }
        for (Report report : reports) {
            report.setStatus(ReportStatus.RESOLVED);
            reportRepository.save(report);
        }
    }

    @Override
    public Post findThisPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void changePostVisibility(Long postId, boolean hidden) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Cập nhật thuộc tính hidden
        post.setHidden(hidden);
        postRepository.save(post);
    }

    @Override
    public List<PostDto> findAllPosts() {
        List<Post> posts = postRepository.findAll(); // Lấy tất cả các bài viết từ repository
        return posts.stream()
                .map(post -> {
                    PostDto postDto = PostMapper.toDto(post); // Chuyển đổi thành PostDto
                    postDto.setUserNickname(post.getUser().getUserInformation().getName()); // Lấy tên người dùng
                    return postDto; // Trả về PostDto đã được thiết lập userName
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> findNewPosts() {
        // Lấy thời gian đầu ngày và cuối ngày hiện tại
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX);

        // Lấy các bài viết trong khoảng thời gian của ngày hôm nay
        List<Post> newPosts = postRepository.findAllByCreatedAtBetween(
                startOfToday, endOfToday, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return newPosts.stream()
                .map(post -> {
                    PostDto postDto = PostMapper.toDto(post); // Chuyển đổi thành PostDto
                    postDto.setUserNickname(post.getUser().getUserInformation().getName()); // Lấy tên người dùng
                    return postDto;
                })
                .collect(Collectors.toList());
    }



    @Override
    public long countTotalPosts() {
        return postRepository.count(); // Sử dụng phương thức count() của PostRepository
    }

    @Override
    public List<PostDto> searchPostsByKeyword(String keyword) {
        // Tìm bài viết theo từ khóa
        List<Post> posts = postRepository.findByKeyword(keyword);

        // Chuyển đổi danh sách Post thành PostDto
        return posts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDto> getReportedPosts() {
        // Lấy danh sách các report từ reportRepository thay vì postRepository
        List<Report> reports = reportRepository.findAll(); // Lấy tất cả các báo cáo

        return reports.stream()
                .map(report -> {
                    ReportDto reportDto = new ReportDto();
                    // Set các thuộc tính của ReportDto từ Report entity
                    reportDto.setId(report.getId()); // ID của báo cáo
                    reportDto.setPostId(report.getPost().getId()); // ID của bài viết bị báo cáo
                    reportDto.setUserId(report.getUser().getId()); // ID của người dùng đã báo cáo
                    reportDto.setReason(report.getReason()); // Lý do báo cáo
                    reportDto.setCreateDate(report.getCreateDate()); // Ngày tạo báo cáo
                    reportDto.setStatus(report.getStatus()); // Trạng thái báo cáo

                    return reportDto; // Trả về ReportDto
                })
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPostByPostId(Long postId) {
        // Tìm bài viết bằng postId
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found")); // Ném ngoại lệ nếu không tìm thấy

        // Chuyển đổi bài viết sang DTO
        return PostMapper.toDto(post);
    }


    @Override
    public List<Post> getFilteredPosts(Long currentUserId) {
        return postRepository.findPostsExcludingBlockedUsers(currentUserId);
    }

    @Override
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bài viết không tồn tại"));
    }
    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public PostDto findPostByIdadmin(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return PostMapper.toDto(post);
    }


    @Override
    public boolean userCanToggleHidden(Long postId, String username) {
        // Tìm bài viết bằng ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại"));

        // Kiểm tra xem người dùng có phải là tác giả của bài viết hay không
        return post.getUser().getUserName().equals(username);
    }

    @Override
    public UserInfoDto getSearchInfo(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        User user = post.getUser(); // lấy user từ post

        return new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getUserInformation().getAvatar(),
                user.getCreateDate().atStartOfDay()
        );
    }
}