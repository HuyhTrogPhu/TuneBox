//package org.example.library.service.implement;
//
//import com.cloudinary.Cloudinary;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class Cloudinary2Service {
//    @Autowired
//    @Qualifier("Cloudinary2")  // Thêm dòng này
//    private Cloudinary cloudinaryy;
//
//    public Map uploadFile(MultipartFile file) throws IOException {
//        return cloudinaryy.uploader()
//                .upload(file.getBytes(),
//                        Map.of("resource_type", "auto"));
//    }
//}
