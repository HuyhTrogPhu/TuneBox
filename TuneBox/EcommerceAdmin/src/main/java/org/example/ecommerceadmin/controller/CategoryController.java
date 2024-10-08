package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryDto;
import org.example.library.service.implement.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    //    Add new category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto,
                                                      MultipartFile image) {
        CategoryDto saveCategory = categoryService.createCategory(categoryDto,image);
        return new ResponseEntity<>(saveCategory, HttpStatus.CREATED);
    }


    //    Get all category
    @GetMapping("/getAllCategory")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> categoryDto = categoryService.getAllCategory();
        return ResponseEntity.ok(categoryDto);
    }


    //    Get category by id
    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("categoryId") Long id) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }


    //    Update category by id
    @PutMapping("{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("categoryId") Long id,
                                                      @RequestBody CategoryDto categoryDto,
                                                      MultipartFile image) {
        CategoryDto saveCategory = categoryService.updateCategory(id, categoryDto,image);
        return ResponseEntity.ok(saveCategory);
    }


    //    Delete category
    @DeleteMapping("{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category successfully");
    }

}
