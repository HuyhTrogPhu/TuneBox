package org.example.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUploadInstrument {

    private final String UPLOAD_FOLDER = "ImageInstrument";

    public String uploadFile(MultipartFile file) {
        String filePath = null;
        try {
            Path uploadPath = Paths.get(UPLOAD_FOLDER);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePathLocation = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePathLocation, StandardCopyOption.REPLACE_EXISTING);
            filePath = filePathLocation.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public boolean checkExist(MultipartFile multipartFile) {
        File file = new File(UPLOAD_FOLDER + multipartFile.getOriginalFilename());
        return file.exists();
    }
}
