package org.example.customer.Controller;


import lombok.AllArgsConstructor;
import org.example.library.dto.CategoryDto;
import org.example.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@AllArgsConstructor
@RequestMapping("/customer/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> list = categoryService.getAllCategory();
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        try {
            CategoryDto category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Instrument not found: " + e.getMessage());
        }
    }


}
