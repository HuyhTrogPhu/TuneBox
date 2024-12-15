package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.library.Exception.ResourceNotFoundException;
import org.example.library.dto.*;
import org.example.library.mapper.PostMapper;
import org.example.library.mapper.ReportMapper;
import org.example.library.model.Post;
import org.example.library.model.PostImage;
import org.example.library.model.Report;
import org.example.library.model.User;
import org.example.library.model_enum.ReportStatus;
import org.example.library.repository.PostRepository;
import org.example.library.repository.ReportRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final Cloudinary cloudinary;
    private final ReportMapper reportMapper;
    private final NotificationServiceImpl notificationServiceImpl;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ReportRepository  reportRepository, Cloudinary cloudinary, ReportMapper reportMapper, NotificationServiceImpl notificationServiceImpl) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.cloudinary = cloudinary;
        this.reportMapper = reportMapper;
        this.notificationServiceImpl = notificationServiceImpl;
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
                .filter(post -> !post.isAdminHidden() && !post.isAdminPermanentlyHidden())
                .map(PostMapper::toDto) // Convert to PostDto
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> get5Posts() {
        return List.of();
    }

    @Override
    public List<PostDto> getPostsByUserId(Long userId, String currentUsername) {
        List<Post> posts;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getUserName().equals(currentUsername)) {
            // Nếu là chủ sở hữu, lấy tất cả bài viết ngoại trừ các bài viết bị admin khóa
            posts = postRepository.findByUserId(userId).stream()
                    .filter(post -> !post.isAdminHidden() && !post.isAdminPermanentlyHidden())
                    .collect(Collectors.toList());
        } else {
            // Nếu không phải chủ sở hữu, chỉ lấy các bài viết không bị ẩn và không bị admin khóa
            posts = postRepository.findByUserIdAndHidden(userId, false).stream()
                    .filter(post -> !post.isAdminHidden() && !post.isAdminPermanentlyHidden())
                    .collect(Collectors.toList());
        }

        return posts.stream().map(PostMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(Long PostId) {
        return null;
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
    public CountNewPostInDayDto findNewPosts() {
        // Lấy thời gian đầu ngày và cuối ngày hôm nay
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX);

        // Đếm số lượng bài viết trong hôm nay
        long count = postRepository.countByCreatedAtBetween(startOfToday, endOfToday);

        // Lấy danh sách bài viết trong hôm nay
        List<Post> newPosts = postRepository.findAllByCreatedAtBetween(
                startOfToday, endOfToday, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // Chuyển đổi danh sách bài viết sang DTO
        List<PostDto> postDtos = newPosts.stream()
                .map(post -> {
                    PostDto postDto = PostMapper.toDto(post);
                    postDto.setUserNickname(post.getUser().getUserInformation().getName());
                    return postDto;
                })
                .collect(Collectors.toList());

        // Tạo đối tượng kết quả
        CountNewPostInDayDto summary = new CountNewPostInDayDto();
        summary.setCount(count);
        summary.setPosts(postDtos);

        return summary;
    }


    @Override
    public List<PostDto> findTrendingPosts() {
        return List.of();
    }


    @Override
    public long countTotalPosts() {
        return postRepository.count();
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
    public PostDto getPostByPostId(Long postId) {
        // Tìm bài viết bằng postId
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found")); // Ném ngoại lệ nếu không tìm thấy

        // Kiểm tra xem bài viết có bị ẩn hoặc bị khóa bởi admin không
        if (post.isAdminHidden() || post.isAdminPermanentlyHidden()) {
            return null; // Trả về null nếu bài viết bị khóa
        }

        // Chuyển đổi bài viết sang DTO
        return PostMapper.toDto(post);
    }


    @Override
    public List<Post> getFilteredPosts(Long currentUserId) {
        // Lấy danh sách bài viết, lọc ra các bài viết bị ẩn hoặc bị khóa bởi admin
        List<Post> posts = postRepository.findPostsExcludingBlockedUsers(currentUserId);
        return posts.stream()
                .filter(post -> !post.isAdminHidden() && !post.isAdminPermanentlyHidden())
                .collect(Collectors.toList());
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

    public Map<LocalDateTime, Long> countPostByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDateTime, Long> postCountMap = new HashMap<>();
        LocalDate currentDate = startDate;
        //for de lay data
        while (!currentDate.isAfter(endDate)) {
            LocalDateTime startOfDay = currentDate.atStartOfDay();
            LocalDateTime endOfDay = currentDate.atTime(23, 59, 59, 999999999); // Thay đổi tại đây
            Long count = postRepository.countByCreatedAtBetween(startOfDay, endOfDay);
            postCountMap.put(startOfDay, count);
            currentDate = currentDate.plusDays(1);
        }

        System.out.println("Post Count Map: " + postCountMap);
        return postCountMap;
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

        // Giả sử InspiredBy, Talent, Genre là các đối tượng có mối quan hệ với User
        List<String> inspiredByUserNames = user.getInspiredBy().stream()
                .map(inspiredBy -> inspiredBy.getName()) // Lấy 'name' từ đối tượng InspiredBy
                .collect(Collectors.toList());

        List<String> talentUserNames = user.getTalent().stream()
                .map(talent -> talent.getName()) // Lấy 'name' từ đối tượng Talent
                .collect(Collectors.toList());

        List<String> genreUserNames = user.getGenre().stream()
                .map(genre -> genre.getName()) // Lấy 'name' từ đối tượng Genre
                .collect(Collectors.toList());
        return new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getUserInformation().getAvatar(),
                user.getCreateDate().atStartOfDay(),
                user.getFollowers().size(),
                user.getFollowing().size(),
                user.getOrderList().size(),
                user.getAlbums().size(),
                user.getTracks().size(),
                inspiredByUserNames,
                talentUserNames,
                genreUserNames
        );
    }

    @Override
    public List<PostDto> findAllPostsUser() {
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
    public Page<PostDto> findPostsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        Page<Post> posts = postRepository.findAllByDateRange(startDateTime, endDateTime, pageable);
        return posts.map(post -> {
            PostDto postDto = PostMapper.toDto(post);
            postDto.setUserNickname(post.getUser().getUserInformation().getName());
            return postDto;
        });
    }

    @Override
    public Page<PostDto> findAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(post -> {
            PostDto postDto = PostMapper.toDto(post);
            postDto.setUserNickname(post.getUser().getUserInformation().getName());
            return postDto;
        });
    }

    @Override
    public Page<PostDto> findPostsBySpecificDate(LocalDate specificDate, Pageable pageable) {
        LocalDateTime startDateTime = specificDate.atStartOfDay();
        LocalDateTime endDateTime = specificDate.atTime(23, 59, 59);
        Page<Post> posts = postRepository.findAllByDateRange(startDateTime, endDateTime, pageable);
        return posts.map(post -> {
            PostDto postDto = PostMapper.toDto(post);
            postDto.setUserNickname(post.getUser().getUserInformation().getName());
            return postDto;
        });
    }

    @Override
    @Transactional
    public Post restorePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + postId));

        if (post.isAdminPermanentlyHidden()) {
            throw new IllegalStateException("Cannot restore permanently hidden post");
        }

        // Đặt lại các trạng thái khi restore
        post.setAdminHidden(false);
        post.setHideReason(null);
        post.setHidden(false);

        // Cập nhật trạng thái của các báo cáo liên quan
        List<Report> reports = reportRepository.findByPostIdAndStatus(postId, ReportStatus.RESOLVED);
        LocalDateTime now = LocalDateTime.now();

        for (Report report : reports) {
            report.setStatus(ReportStatus.DISMISSED);
            report.setResolvedAt(now);
            report.setDescription("Report closed due to post restoration at: " + now);
            reportRepository.save(report);
        }

        // Lưu và trả về bài viết đã được khôi phục
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public List<Report2Dto> dismissAllReports(Long postId, String reason) {
        log.info("Starting dismissAllReports for postId: {} with reason: {}", postId, reason);

        // Validate input parameters
        if (postId == null) {
            log.error("PostId cannot be null");
            throw new IllegalArgumentException("PostId cannot be null");
        }

        if (reason == null || reason.trim().isEmpty()) {
            log.error("Dismiss reason cannot be empty for postId: {}", postId);
            throw new IllegalArgumentException("Dismiss reason cannot be empty");
        }

        try {
            // Kiểm tra post tồn tại
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> {
                        log.error("Post not found with id: {}", postId);
                        return new ResourceNotFoundException("Post not found: " + postId);
                    });

            // Lấy tất cả các report PENDING của post
            List<Report> reports = reportRepository.findByPostIdAndStatus(postId, ReportStatus.PENDING);
            log.info("Found {} pending reports for postId: {}", reports.size(), postId);

            if (reports.isEmpty()) {
                log.warn("No pending reports found for postId: {}", postId);
                throw new ResourceNotFoundException("No pending reports found for post: " + postId);
            }

            LocalDateTime now = LocalDateTime.now();
            List<Report2Dto> dismissedReports = new ArrayList<>();

            try {
                // Đảm bảo post không bị ẩn
                log.debug("Updating post status for postId: {}", postId);
                post.setAdminHidden(false);
                post.setHideReason(null);
                postRepository.save(post);
            } catch (Exception e) {
                log.error("Error updating post status for postId: {}", postId, e);
                throw new RuntimeException("Failed to update post status", e);
            }

            // Xử lý từng report
            for (Report report : reports) {
                try {
                    log.debug("Processing report id: {} for postId: {}", report.getId(), postId);
                    report.setStatus(ReportStatus.DISMISSED);
                    report.setReason(reason);
                    report.setResolvedAt(now);
                    report.setDescription("Report dismissed at: " + now + ". Reason: " + reason);

                    Report savedReport = reportRepository.save(report);

                    // Convert to DTO including report details
                    Report2Dto reportDto = reportMapper.toReport2Dto(savedReport);

                    // Add report details
                    List<ReportDetailDto> details = new ArrayList<>();
                    details.add(new ReportDetailDto(
                            report.getId(),
                            report.getUser().getUserName(),
                            report.getReason(),
                            report.getCreateDate()
                    ));
                    reportDto.setReportDetails(details);

                    dismissedReports.add(reportDto);

                    // Tạo thông báo cho người báo cáo
                    try {
                        notificationServiceImpl.createNotificationForUser(
                                report.getUser(),
                                "Cảm ơn bạn đã gửi báo cáo. Chúng tôi đã xem xét và quyết định bỏ qua báo cáo này.",
                                "REPORT_DISMISSED"
                        );
                    } catch (Exception e) {
                        log.error("Failed to create notification for report id: {} and user: {}",
                                report.getId(), report.getUser().getId(), e);
                    }
                } catch (Exception e) {
                    log.error("Error processing report id: {} for postId: {}", report.getId(), postId, e);
                }
            }

            log.info("Successfully dismissed {} reports for postId: {}", dismissedReports.size(), postId);
            return dismissedReports;

        } catch (Exception e) {
            log.error("Unexpected error in dismissAllReports for postId: {}", postId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Post resolvePost(Long reportId, String reason) {
        log.info("Starting resolvePost for reportId: {} with reason: {}", reportId, reason);

        if (reportId == null) {
            throw new IllegalArgumentException("ReportId cannot be null");
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Resolve reason cannot be empty");
        }

        try {
            Report report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + reportId));

            Post post = report.getPost();
            if (post == null) {
                throw new ResourceNotFoundException("Post not found for report: " + reportId);
            }

            // Lấy tất cả các report PENDING của post
            List<Report> relatedReports = reportRepository.findByPostIdAndStatus(post.getId(), ReportStatus.PENDING);

            LocalDateTime now = LocalDateTime.now();

            // Cập nhật post
            post.setAdminHidden(true);
            post.setHidden(true);
            post.setHideReason(reason);

            // Cập nhật tất cả các report liên quan
            for (Report relatedReport : relatedReports) {
                relatedReport.setStatus(ReportStatus.RESOLVED);
                relatedReport.setResolvedAt(now);
                relatedReport.setDescription("Resolved due to post action at: " + now + ". Reason: " + reason);

                try {
                    reportRepository.save(relatedReport);

                    // Gửi thông báo cho người báo cáo
                    notificationServiceImpl.createNotificationForUser(
                            relatedReport.getUser(),
                            "Báo cáo của bạn đã được xử lý. Bài viết đã bị ẩn.",
                            "REPORT_RESOLVED"
                    );
                } catch (Exception e) {
                    log.error("Error updating related report: {}", relatedReport.getId(), e);
                }
            }

            return postRepository.save(post);

        } catch (Exception e) {
            log.error("Unexpected error in resolvePost for reportId: {}", reportId, e);
            throw e;
        }
    }

    @Override
    public List<Report2Dto> getPostReports(Long postId) {
        log.info("Getting reports for post: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + postId));

        List<Report> reports = reportRepository.findByPostId(postId);

        return reports.stream()
                .map(report -> {
                    Report2Dto dto = reportMapper.toReport2Dto(report);
                    List<ReportDetailDto> details = new ArrayList<>();
                    details.add(new ReportDetailDto(
                            report.getId(),
                            report.getUser().getUserName(),
                            report.getReason(),
                            report.getCreateDate()
                    ));
                    dto.setReportDetails(details);
                    return dto;
                })
                .collect(Collectors.toList());
    }

}