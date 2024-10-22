package org.example.library.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    // Lấy giá trị từ application.properties
    @Value("${file.upload-dir:uploads}") // mặc định là "uploads" nếu không có trong properties
    private String uploadDir;

    // Constructor của service
    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        // Chuyển đường dẫn tương đối thành tuyệt đối và chuẩn hóa
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            // Tạo thư mục nếu chưa tồn tại
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Không thể tạo thư mục để lưu trữ files.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            // Kiểm tra file
            if (file.isEmpty()) {
                throw new RuntimeException("File trống");
            }

            // Kiểm tra kích thước file (ví dụ: max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new RuntimeException("File quá lớn (>10MB)");
            }

            // Lấy tên file gốc và làm sạch
            String originalFileName = StringUtils.cleanPath(
                    file.getOriginalFilename());

            // Kiểm tra tên file hợp lệ
            if (originalFileName.contains("..")) {
                throw new RuntimeException(
                        "Tên file không hợp lệ " + originalFileName);
            }

            // Tạo tên file unique bằng UUID
            String fileExtension = "";
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(
                        originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Đường dẫn đầy đủ đến file
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

            // Copy file vào thư mục đích
            Files.copy(file.getInputStream(), targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;

        } catch (IOException ex) {
            throw new RuntimeException(
                    "Không thể lưu file. Vui lòng thử lại!", ex);
        }
    }

    // Phương thức lấy đường dẫn file
    public Path getFilePath(String fileName) {
        return fileStorageLocation.resolve(fileName).normalize();
    }

    // Phương thức kiểm tra file tồn tại
    public boolean fileExists(String fileName) {
        Path filePath = getFilePath(fileName);
        return Files.exists(filePath);
    }
}