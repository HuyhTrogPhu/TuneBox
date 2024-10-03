package org.example.ecommerceadmin.controller;

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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/instrument")
public class InstrumentController {

    @Autowired
    private InstrumentServiceImpl instrumentService;

    @Autowired
    private BrandServiceImpl brandService;

    @Autowired
    private CategoryServiceImpl categoryService;

    // Add new instrument
    @PostMapping
    public ResponseEntity<InstrumentDto> createInstrument(@RequestBody InstrumentDto instrumentDto,
                                              @RequestParam("image") MultipartFile image) {
        try {
            InstrumentDto saveInstrument = instrumentService.createInstrument(instrumentDto, image);
            return new ResponseEntity<>(saveInstrument, HttpStatus.CREATED);
        } catch (Exception e) {
            e.getStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    // Update Instrument
    @PutMapping("{id}")
    public ResponseEntity<?> updateInstrument(@PathVariable Long id,
                                              @RequestBody InstrumentDto instrumentDto,
                                              @RequestParam("insImage") MultipartFile image) {
        try {
            InstrumentDto saveInstrument = instrumentService.updateInstrument(id, instrumentDto, image);
            return ResponseEntity.ok(saveInstrument);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating instrument: " + e.getMessage());
        }
    }

    // Delete instrument
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteInstrument(@PathVariable Long id) {
        try {
            instrumentService.deleteInstrument(id);
            return ResponseEntity.ok("Delete instrument successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting instrument: " + e.getMessage());
        }
    }
}
