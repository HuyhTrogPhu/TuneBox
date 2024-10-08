package org.example.ecommerceadmin.controller;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.AllArgsConstructor;

import org.example.library.dto.BrandsDto;
import org.example.library.model.Brand;
import org.example.library.service.implement.BrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/brand")
public class BrandController {

    @Autowired
    private BrandServiceImpl brandService;

    //    Add new brand
    @PostMapping
    public ResponseEntity<BrandsDto> createBrand(@RequestParam("name") String name,
                                                 @RequestParam("imageBrand") MultipartFile image,
                                                 @RequestParam("desc") String description
    ) {
        BrandsDto brandsDto = new BrandsDto();
        brandsDto.setName(name);
        brandsDto.setDescription(description);
        brandsDto.setStatus(true);

        BrandsDto saveBrand = brandService.createBrand(brandsDto, image);
        return new ResponseEntity<>(saveBrand, HttpStatus.CREATED);
    }


    //    Get all brand
    @GetMapping
    public ResponseEntity<List<BrandsDto>> getAllBrand() {
        List<BrandsDto> brandsDto = brandService.getAllBrand();
        return ResponseEntity.ok(brandsDto);
    }


    //    Get brand by id
    @GetMapping("{id}")
    public ResponseEntity<BrandsDto> getBrand(@PathVariable Long id) {
        BrandsDto brandsDto = brandService.getBrandById(id);
        return ResponseEntity.ok(brandsDto);
    }


    //    Update brand
    @PutMapping("{id}")
    public ResponseEntity<BrandsDto> updateBrand(
            @RequestParam("name") String name,
            @RequestParam("desc") String description,
            @RequestParam(value = "imageBrand", required = false) MultipartFile image,
            @RequestParam(value = "status") Boolean status,
            @PathVariable Long id) {

        // Tạo BrandsDto mới với thông tin cập nhật
        BrandsDto brandsDto = new BrandsDto();
        brandsDto.setName(name);
        brandsDto.setDescription(description);
        brandsDto.setStatus(status);

        // Gọi phương thức cập nhật thưdgdssfsdfdsfdsdơng hiệu
        BrandsDto updatedBrand = brandService.updateBrand(id, brandsDto, image);
        return ResponseEntity.ok(updatedBrand);
    }




    //    Delete brand
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok("Delete brand successfully");
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<BrandsDto>> getBrandsByKeyword(@PathVariable String keyword) {
        List<BrandsDto> brandsDto = brandService.searchBrand(keyword);
        return ResponseEntity.ok(brandsDto);
    }

}
