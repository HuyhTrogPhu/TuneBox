package org.example.ecommerceadmin.controller;

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


@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/brand")
public class BrandController {

    @Autowired
    private BrandServiceImpl brandService;

    //    Add new brand
    @PostMapping
    public ResponseEntity<BrandsDto> createBrand(@RequestBody BrandsDto brandsDto,
                                                 @RequestParam("imageBrand") MultipartFile image) {
        BrandsDto saveBrand = brandService.createBrand(brandsDto, image);
        return new ResponseEntity<>(saveBrand, HttpStatus.CREATED);
    }


    //    Get all brand
    @GetMapping("/getAllBrand")
    public ResponseEntity<List<BrandsDto>> getAllBrand() {
        List<BrandsDto> brandsDto = brandService.getAllBrand();
        return ResponseEntity.ok(brandsDto);
    }


    //    Get brand by id
    @GetMapping("{brandId}")
    public ResponseEntity<BrandsDto> getBrand(@PathVariable("brandId") Long brandId) {
        BrandsDto brandsDto = brandService.getBrandById(brandId);
        return ResponseEntity.ok(brandsDto);
    }


    //    Update brand
    @PutMapping("{brandId}")
    public ResponseEntity<BrandsDto> updateBrand(@RequestBody BrandsDto brandsDto,
                                                 @PathVariable("brandId") Long id,
                                                 @RequestParam("imageBrand") MultipartFile image) {
        BrandsDto saveBrand = brandService.updateBrand(id, brandsDto, image);
        return ResponseEntity.ok(saveBrand);
    }


    //    Delete brand
    @DeleteMapping("{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable("brandId") Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok("Delete brand successfully");
    }


}
