package org.example.customer.controller;


import lombok.AllArgsConstructor;
import org.example.library.dto.*;
import org.example.library.service.BrandService;
import org.example.library.service.CategoryService;
import org.example.library.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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


    @GetMapping("/instruments/search")
    public ResponseEntity<List<InstrumentDto>> searchInstruments(@RequestParam("keyword") String keyword) {
        List<InstrumentDto> instrumentDtos = instrumentService.searchInstruments(keyword);

        // In thông tin các InstrumentDto tìm được ra console
        System.out.println("Instruments found: ");
        for (InstrumentDto instrumentDto : instrumentDtos) {
            System.out.println(instrumentDto);
        }

        return ResponseEntity.ok(instrumentDtos);
    }


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


    //   Get instrument by category id and brand id
    @GetMapping("/detailInstruments/")
    public ResponseEntity<List<InstrumentDto>> getInstrumentsByCategoryAndBrand(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId) {

        try {
            // Nếu cả categoryId và brandId đều không được truyền vào, trả về tất cả instruments
            if (categoryId == null && brandId == null) {
                return ResponseEntity.ok(instrumentService.getAllInstrument());
            }

            // Nếu có tham số, tìm kiếm theo category và brand
            List<InstrumentDto> instruments = instrumentService.getInstrumentByCategoryIdAndBrandId(categoryId, brandId);
            return ResponseEntity.ok(instruments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/instruments/{id}/quantity")
    public ResponseEntity<?> getInstrumentQuantity(@PathVariable Long id) {
        try {
            Integer quantity = instrumentService.getInstrumentQuantityById(id);
            if (quantity != null) {
                return ResponseEntity.ok(quantity);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Instrument not found or quantity not available");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving instrument quantity: " + e.getMessage());
        }
    }



}