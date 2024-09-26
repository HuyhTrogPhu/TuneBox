package org.example.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class ImageUpload {

    private final String UPLOAD_FOLDER = "Image";

    // Upload multiple files
    public boolean uploadFiles(List<MultipartFile> files) {
        boolean isAllUploaded = true; // Biến để theo dõi trạng thái upload

        for (MultipartFile file : files) {
            if (!uploadFile(file)) {
                isAllUploaded = false; // Nếu bất kỳ file nào không upload thành công, đánh dấu false
            }
        }

        return isAllUploaded;
    }

    // Upload a single file
    public boolean uploadFile(MultipartFile file) {
        boolean isUpload = false;
        try {
            Files.copy(file.getInputStream(),
                    Paths.get(UPLOAD_FOLDER + File.separator + file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpload;
    }

    // Check if a single file exists
    public boolean checkExist(MultipartFile multipartFile) {
        boolean isExist = false;
        try {
            File file = new File(UPLOAD_FOLDER + File.separator + multipartFile.getOriginalFilename());
            isExist = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    // Check if multiple files exist
    public boolean checkFilesExist(List<MultipartFile> files) {
        boolean areAllExist = true;

        for (MultipartFile file : files) {
            if (!checkExist(file)) {
                areAllExist = false; // Nếu bất kỳ file nào không tồn tại, đánh dấu false
            }
        }

        return areAllExist;
    }
}
