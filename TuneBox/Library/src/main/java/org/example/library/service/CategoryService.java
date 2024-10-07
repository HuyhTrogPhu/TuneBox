package org.example.library.service;


import org.example.library.dto.CategoryDto;
import org.example.library.model.CategoryIns;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto, MultipartFile image);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAllCategory();

    CategoryDto updateCategory(Long id, CategoryDto categoryDto, MultipartFile image);

    void changeCategoryStatus(Long id);

    void deleteCategory(Long id);

    CategoryIns getManagedCategory(Long id);
}