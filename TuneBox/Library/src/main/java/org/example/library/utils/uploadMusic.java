package org.example.library.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class uploadMusic {

    private final String UPLOAD_FOLDER = "AudioTrack"; // Tên thư mục tải lên

    public uploadMusic() {
        // Tạo thư mục nếu nó không tồn tại
        File uploadDir = new File(UPLOAD_FOLDER);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs(); // Sử dụng mkdirs() để tạo tất cả thư mục cha
            if (!created) {
                throw new RuntimeException("Failed to create upload directory: " + UPLOAD_FOLDER);
            }
        }
    }

    public boolean uploadMusic(MultipartFile file) {
        try {
            // Kiểm tra loại tệp (tuỳ chọn)
            if (!file.getContentType().startsWith("audio/")) {
                throw new IOException("File is not an audio type");
            }

            Path targetLocation = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkExist(MultipartFile multipartFile) {
        File file = new File(UPLOAD_FOLDER, multipartFile.getOriginalFilename());
        return file.exists();
    }
}
