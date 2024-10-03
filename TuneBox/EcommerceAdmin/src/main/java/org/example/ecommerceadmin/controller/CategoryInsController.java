package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryInsDTO;
import org.example.library.service.CategoryInsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/categoryIns")
public class CategoryInsController {

    @Autowired
    private CategoryInsService categoryInsService;

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryInsDTO>> getAllCategories() {
        List<CategoryInsDTO> categories = categoryInsService.findAll();
        return ResponseEntity.ok(categories);
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryInsDTO> getCategoryById(@PathVariable Long id) {
        CategoryInsDTO category = categoryInsService.findById(id);
        return ResponseEntity.ok(category);
    }

    // Create new category
    @PostMapping
    public ResponseEntity<CategoryInsDTO> createCategory(@RequestBody CategoryInsDTO categoryInsDTO) {
        CategoryInsDTO newCategory = categoryInsService.create(categoryInsDTO);
        return ResponseEntity.ok(newCategory);
    }

    // Update existing category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryInsDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryInsDTO categoryInsDTO) {
        CategoryInsDTO updatedCategory = categoryInsService.update(id, categoryInsDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryInsService.deleteCateIns(id);
        return ResponseEntity.noContent().build();  // No content response
    }
}
