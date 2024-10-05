package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.dto.CategoryDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.model.Brand;
import org.example.library.model.CategoryIns;
import org.example.library.service.BrandService;
import org.example.library.service.CategoryService;
import org.example.library.service.InstrumentService;
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
    private InstrumentService instrumentService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;


    // Add new instrument
    @PostMapping
    public ResponseEntity<InstrumentDto> createInstrument(@RequestParam("name") String name,
                                              @RequestParam("costPrice") double costPrice,
                                              @RequestParam("quantity") int quantity,
                                              @RequestParam("color") String color,
                                              @RequestParam("description") String description,
                                              @RequestParam("brandId") Brand brand,
                                              @RequestParam("categoryId") CategoryIns category,
                                                          @RequestParam(value = "image", required = false) MultipartFile[] image)  {
        try {
            InstrumentDto instrumentDto = new InstrumentDto();
            instrumentDto.setName(name);
            instrumentDto.setCostPrice(costPrice);
            instrumentDto.setQuantity(quantity);
            instrumentDto.setColor(color);
            instrumentDto.setDescription(description);
            instrumentDto.setBrand(brand);
            instrumentDto.setCategoryIns(category);

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
    // Update Instrument
    @PutMapping("{id}")
    public ResponseEntity<?> updateInstrument(
            @PathVariable String id,  // Nhận id dưới dạng String từ frontend
            @RequestParam("name") String name,
            @RequestParam("costPrice") String costPrice,
            @RequestParam("quantity") String quantity,
            @RequestParam("color") String color,
            @RequestParam("description") String description,
            @RequestParam("brandId") Long brandId,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("status") boolean status,
            @RequestParam(value = "image", required = false) MultipartFile[] image) {

        try {
            // Kiểm tra nếu id là "undefined" hoặc rỗng
            if (id == null || id.equals("undefined") || id.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid instrument ID: " + id);
            }

            // Chuyển đổi id từ String sang Long
            Long instrumentId = Long.parseLong(id);

            // Tìm nhạc cụ theo id
            InstrumentDto existingInstrument = instrumentService.getInstrumentById(instrumentId);

            // Cập nhật thông tin nhạc cụ
            existingInstrument.setName(name);
            existingInstrument.setCostPrice(Double.parseDouble(costPrice));
            existingInstrument.setQuantity(Integer.parseInt(quantity));
            existingInstrument.setColor(color);
            existingInstrument.setDescription(description);
            existingInstrument.setStatus(status);

            // Tìm thương hiệu và danh mục
            Brand brand = brandService.getManagedBrand(brandId);
            CategoryIns category = categoryService.getManagedCategory(categoryId);

            // Cập nhật thông tin thương hiệu và danh mục
            existingInstrument.setBrand(brand);
            existingInstrument.setCategoryIns(category);

            // Lưu thay đổi
            InstrumentDto saveInstrument = instrumentService.updateInstrument(instrumentId, existingInstrument, image);
            return ResponseEntity.ok(saveInstrument);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid instrument ID format: " + id);
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
