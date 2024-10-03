package org.example.library.mapper;

import org.example.library.dto.CategoryInsDTO;
import org.example.library.model.CategoryIns;

public class CategoryInsMapper {
    public static CategoryInsDTO toDTO(CategoryIns categoryIns) {
        return new CategoryInsDTO(
                categoryIns.getId(),
                categoryIns.getName(),
                categoryIns.isStatus()
        );
    }

    // Convert DTO to Entity
    public static CategoryIns toEntity(CategoryInsDTO categoryInsDTO) {
        return new CategoryIns(
                categoryInsDTO.getId(),
                categoryInsDTO.getName(),
                categoryInsDTO.isStatus()
        );
    }
}
