package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryDto;
import org.example.library.model.CategoryIns;
import org.example.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/categoryIns")
public class CategoryInsController {

    @Autowired
    private CategoryService categoryService;

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // Create new category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("image") MultipartFile image) {
        // Tạo đối tượng CategoryDto
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        categoryDto.setDescription(description);

        // Gọi phương thức để tạo category và tải lên hình ảnh
        CategoryDto newCategory = categoryService.createCategory(categoryDto, image);

        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // Update existing category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,
                                                      @RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("status") Boolean status,
                                                      @RequestParam(value = "image", required = false) MultipartFile image) {
        // Tạo đối tượng CategoryDto
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        categoryDto.setDescription(description);
        categoryDto.setStatus(status);

        // Gọi phương thức cập nhật category
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto, image);
        return ResponseEntity.ok(updatedCategory);
    }

    // Change category status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeCategoryStatus(@PathVariable Long id) {
        categoryService.changeCategoryStatus(id);
        return ResponseEntity.noContent().build();  // No content response
    }

    // Delete category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();  // No content response
    }
}
