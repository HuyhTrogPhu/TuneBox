package org.example.customer.controller;


import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.dto.CategoryDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.model.CategoryIns;
import org.example.library.model.Instrument;
import org.example.library.service.BrandService;
import org.example.library.service.CategoryService;
import org.example.library.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/customer/shop")
public class ShopController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InstrumentService instrumentService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> list = categoryService.getAllCategory();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandsDto>> getBrands() {
        List<BrandsDto> list = brandService.getAllBrand();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/instruments")
    public ResponseEntity<List<InstrumentDto>> getInstruments() {
        try {
            List<InstrumentDto> list = instrumentService.getAllInstrument();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.getStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    Get instruments detail from shop
    @GetMapping("{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id) {
        try {
            InstrumentDto instrumentDto = instrumentService.getInstrumentById(id);
            return ResponseEntity.ok(instrumentDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Instrument not found: " + e.getMessage());
        }
    }
}
