package org.example.library.mapper;


import org.example.library.dto.CategoryDto;
import org.example.library.model.CategoryIns;

public class CategoryMapper {

    public static CategoryDto mapperCategoryDto(CategoryIns category) {
        return new CategoryDto(category.getId(), category.getName(),
                category.getImage(), category.getDescription(), category.isStatus());
    }

    public static CategoryIns mapperCategory(CategoryDto categoryDto) {
        return new CategoryIns(categoryDto.getId(), categoryDto.getName(),
                categoryDto.getImage(), categoryDto.getDescription(), categoryDto.isStatus());
    }
}
