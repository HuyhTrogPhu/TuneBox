//package org.example.library.service.implement;
//
//
//import org.example.library.dto.CategoryInsDto;
//import org.example.library.mapper.CategoryInsMapper;
//import org.example.library.model.CategoryIns;
//import org.example.library.repository.CategoryInsRepository;
//import org.example.library.service.CategoryInsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class CategoryInsServiceImpl implements CategoryInsService {
//    @Autowired
//    private CategoryInsRepository categoryInsRepository;
//
//    @Override
//    public List<CategoryInsDto> findAll() {
//        return categoryInsRepository.findAll().stream()
//                .map(CategoryInsMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//        public CategoryInsDto findById(Long id) {
//        return categoryInsRepository.findById(id)
//                .map(CategoryInsMapper::toDTO)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//    }
//
//    @Override
//    public CategoryInsDto create(CategoryInsDto categoryInsDTO) {
//        CategoryIns categoryIns = CategoryInsMapper.toEntity(categoryInsDTO);
//        categoryIns = categoryInsRepository.save(categoryIns);
//        return CategoryInsMapper.toDTO(categoryIns);
//    }
//
//    @Override
//    public CategoryInsDto update(Long id, CategoryInsDto categoryInsDTO) {
//        CategoryIns existingCategory = categoryInsRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//        existingCategory.setName(categoryInsDTO.getName());
//        existingCategory.setStatus(categoryInsDTO.isStatus());
//        existingCategory = categoryInsRepository.save(existingCategory);
//        return CategoryInsMapper.toDTO(existingCategory);
//    }
//
//
//
//    @Override
//    public void deleteCateIns(Long id) {
//        CategoryIns categoryIns = categoryInsRepository.findById(id).orElseThrow(
//                () -> new RuntimeException("Brand not found")
//        );
//        categoryIns.setStatus(false);
//        categoryInsRepository.save(categoryIns);
//    }
//}
