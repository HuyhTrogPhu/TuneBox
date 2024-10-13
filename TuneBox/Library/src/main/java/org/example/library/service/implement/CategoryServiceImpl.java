package org.example.library.service.implement;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryDto;
import org.example.library.mapper.CategoryMapper;
import org.example.library.model.CategoryIns;
import org.example.library.repository.CategoryInsRepository;
import org.example.library.service.CategoryService;
import org.example.library.utils.ImageUploadCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryInsRepository categoryInsRepository;
    @Autowired
    private Cloudinary cloudinary;
    private final ImageUploadCategory imageUploadCategory;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto, MultipartFile image) {
        try {
            // Tải ảnh lên Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());

            // Lưu URL của ảnh vào categoryDto
            String imageUrl  = (String) uploadResult.get("url");
            categoryDto.setImage(imageUrl );

            // Tạo và lưu category trong cơ sở dữ liệu
            CategoryIns category = new CategoryIns();
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
            category.setImage(imageUrl);
            category.setStatus(false);
            categoryInsRepository.save(category);

            return categoryDto;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryInsRepository.findById(id)
                .map(CategoryMapper::mapperCategoryDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<CategoryIns> list = categoryInsRepository.findAll();
        return list.stream().map(CategoryMapper::mapperCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto, MultipartFile image) {
        try {
            CategoryIns categoryIns = categoryInsRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Category not found")
            );

            categoryIns.setName(categoryDto.getName());
            categoryIns.setDescription(categoryDto.getDescription());
            categoryIns.setStatus(categoryDto.isStatus());

            // Nếu có hình ảnh mới, xóa ảnh cũ và tải ảnh mới
            if (image != null && !image.isEmpty()) {
                // Xóa ảnh cũ trên Cloudinary (nếu cần)
                if (categoryIns.getImage() != null) {
                    String publicId = extractPublicIdFromUrl(categoryIns.getImage());
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                }

                // Tải ảnh mới lên Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("url");

                // Cập nhật URL của ảnh mới
                categoryIns.setImage(imageUrl);
            }

            CategoryIns savedCategory = categoryInsRepository.save(categoryIns);
            return CategoryMapper.mapperCategoryDto(savedCategory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Update fail", e);
        }
    }

    // Phương thức để lấy publicId từ URL ảnh
    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1];
        return publicIdWithExtension.split("\\.")[0];  // Lấy publicId trước phần mở rộng ảnh (ví dụ: .jpg)
    }


    @Override
    public void changeCategoryStatus(Long id) {
        CategoryIns categoryIns = categoryInsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found")
        );
        categoryIns.setStatus(!categoryIns.isStatus());  // Chuyển đổi trạng thái
        categoryInsRepository.save(categoryIns);
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryIns categoryIns = categoryInsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found")
        );
        categoryIns.setStatus(false);
        categoryInsRepository.save(categoryIns);
    }

    @Override
    public CategoryIns getManagedCategory(Long id) {
        return categoryInsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }


}
