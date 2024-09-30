package org.example.library.service;

import org.example.library.dto.CategoryInsDto;

import java.util.List;

public interface CategoryInsService {
    List<CategoryInsDto> findAll();
    CategoryInsDto findById(Long id);
    CategoryInsDto create(CategoryInsDto categoryInsDTO);
    CategoryInsDto update(Long id, CategoryInsDto categoryInsDTO);
    void deleteCateIns(Long id);
}
