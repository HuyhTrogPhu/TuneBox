package org.example.library.service;

import org.example.library.dto.CategoryInsDTO;
import org.example.library.model.CategoryIns;

import java.util.List;

public interface CategoryInsService {
    List<CategoryInsDTO> findAll();
    CategoryInsDTO findById(Long id);
    CategoryInsDTO create(CategoryInsDTO categoryInsDTO);
    CategoryInsDTO update(Long id, CategoryInsDTO categoryInsDTO);
    void deleteCateIns(Long id);
    CategoryIns getManagedCategory(Long id);
}
