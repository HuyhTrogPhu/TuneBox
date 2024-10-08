package org.example.customer.controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.dto.CategoryDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.service.implement.BrandServiceImpl;
import org.example.library.service.implement.CategoryServiceImpl;
import org.example.library.service.implement.InstrumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/customer/instrument")
public class InstrumentController {
    @Autowired
    private InstrumentServiceImpl instrumentService;

    @Autowired
    private BrandServiceImpl brandService;

    @Autowired
    private CategoryServiceImpl categoryService;


    // Get all instruments
    @GetMapping
    public ResponseEntity<List<InstrumentDto>> getAll() {
        List<InstrumentDto> instruments = instrumentService.getAllInstrument();
        return ResponseEntity.ok(instruments);
    }

    // Get all brands
    @GetMapping("/brands")
    public ResponseEntity<List<BrandsDto>> getAllBrand() {
        List<BrandsDto> brandsDto = brandService.getAllBrand();
        return ResponseEntity.ok(brandsDto);
    }
    //get all brand id instrument
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<InstrumentDto>> getInstrumentsByBrandId(@PathVariable Long brandId) {
        List<InstrumentDto> instruments = instrumentService.getInstrumentsByBrandId(brandId);
        return ResponseEntity.ok(instruments);
    }

    // Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> categoryDto = categoryService.getAllCategory();
        return ResponseEntity.ok(categoryDto);
    }
    // Get instrument by id
    @GetMapping("{id}")
    public ResponseEntity<?> getInstrumentById(@PathVariable Long id) {
        try {
            InstrumentDto instrumentDto = instrumentService.getInstrumentById(id);
            return ResponseEntity.ok(instrumentDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Instrument not found: " + e.getMessage());
        }
    }
}
