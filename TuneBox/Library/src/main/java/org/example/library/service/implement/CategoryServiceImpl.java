package org.example.library.service.implement;

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
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryInsRepository categoryInsRepository;

    private final ImageUploadCategory imageUploadCategory;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto, MultipartFile image) {
        try {
            CategoryIns categoryIns = CategoryMapper.mapperCategory(categoryDto);

            if(image != null ){
                imageUploadCategory.uploadFile(image);
                categoryIns.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
            }

            categoryIns.setStatus(true);
            CategoryIns saveCategory = categoryInsRepository.save(categoryIns);
            return CategoryMapper.mapperCategoryDto(saveCategory);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
            categoryIns.setStatus(true);

            if(image != null ) {
                imageUploadCategory.uploadFile(image);
                categoryIns.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
            }

            CategoryIns saveCategory = categoryInsRepository.save(categoryIns);
            return CategoryMapper.mapperCategoryDto(saveCategory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Update fail");
        }
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
        return categoryInsRepository.findById(id) // Đúng rồi, sử dụng instance
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }
}
