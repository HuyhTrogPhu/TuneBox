package org.example.library.service;


import org.example.library.dto.CategoryDto;
import org.example.library.model.CategoryIns;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAllCategory();

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryIns getManagedCategory(Long id);
}
