package org.example.library.service.implement;

import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryDto;
import org.example.library.mapper.CategoryMapper;
import org.example.library.model.CategoryIns;
import org.example.library.repository.CategoryInsRepository;
import org.example.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryInsRepository categoryInsRepository;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        CategoryIns categoryIns = CategoryMapper.mapperCategory(categoryDto);
        categoryIns.setStatus(true);
        CategoryIns saveCategory = categoryInsRepository.save(categoryIns);
        return CategoryMapper.mapperCategoryDto(saveCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        CategoryIns categoryIns = categoryInsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not fond")
        );
        return CategoryMapper.mapperCategoryDto(categoryIns);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<CategoryIns> list = categoryInsRepository.findAll();
        return list.stream().map(CategoryMapper::mapperCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        CategoryIns categoryIns = categoryInsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found")
        );
        categoryIns.setName(categoryDto.getName());
        categoryIns.setStatus(true);
        CategoryIns saveCategory = categoryInsRepository.save(categoryIns);
        return CategoryMapper.mapperCategoryDto(saveCategory);
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
