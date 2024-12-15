package org.example.library.service;


import org.example.library.dto.CategoryDto;
import org.example.library.dto.CategorySalesDto;
import org.example.library.dto.StatisticalCategoryDto;
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

    // get list category sales the most of day
    List<CategorySalesDto> getCategorySalesMostOfDay();

    // get list category sales the most of week
    List<CategorySalesDto> getCategorySalesMostOfWeek();

    // get list category sales the most of month
    List<CategorySalesDto> getCategorySalesMostOfMonth();

    // get list category sales the least of day
    List<CategorySalesDto> getCategorySalesLeastOfDay();

    // get list category sales the least of week
    List<CategorySalesDto> getCategorySalesLeastOfWeek();

    // get list category sales the least of month
    List<CategorySalesDto> getCategorySalesLeastOfMonth();

    // get id and name category
    List<StatisticalCategoryDto> getIdsAndNamesCategory();

    // get revenue category by category id of day
    Double getRevenueCategoryByDay(Long id);

    // get revenue category by category id of week
    Double getRevenueCategoryByWeek(Long id);

    // get revenue category by category id of month
    Double getRevenueCategoryByMonth(Long id);

    // get revenue category by category id of year
    Double getRevenueCategoryByYear(Long id);


}


