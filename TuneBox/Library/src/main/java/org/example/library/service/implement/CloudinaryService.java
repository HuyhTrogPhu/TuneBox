package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    @Qualifier("myCloudinary")  // Thêm dòng này
    private Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file) throws IOException {
        return cloudinary.uploader()
                .upload(file.getBytes(), 
                        Map.of("resource_type", "auto"));
    }
}