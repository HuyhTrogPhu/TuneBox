package org.example.library.utils;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUploadBrand {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        return cloudinary.uploader()
                .upload(file.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }



//    private final String UPLOAD_FOLDER = "ImageBrand";

//    public boolean uploadFile(MultipartFile file) {
//        boolean isUpload = false;
//        try {
//            Path uploadPath = Paths.get(UPLOAD_FOLDER);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            Files.copy(file.getInputStream(), uploadPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//            isUpload = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return isUpload;
//    }

//    public boolean checkExist(MultipartFile multipartFile) {
//        boolean isExist = false;
//        try {
//            File file = new File(UPLOAD_FOLDER +"\\" + multipartFile.getOriginalFilename());
//            isExist = file.exists();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return isExist;
//    }
}
