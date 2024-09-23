package org.example.ecommerceadmin.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.library.dto.BrandsDto;
import org.example.library.model.Brand;
import org.example.library.service.BrandService;
import org.example.library.service.implement.BrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/e-comAdmin/brand")
public class BrandController {

    @Autowired
    public BrandService brandService;


//    Add brand
    @PostMapping
    public ResponseEntity<BrandsDto> createBrand(@RequestBody BrandsDto brandsDto){
        BrandsDto saveBrand = brandService.createBrand(brandsDto);
        return new ResponseEntity<>(saveBrand, HttpStatus.CREATED);
    }


//    Get all brand
    @GetMapping("/brands")
    public  ResponseEntity<List<BrandsDto>> getAllBrands(){
        List<BrandsDto> brandsDto = brandService.getAllBrand();
        return ResponseEntity.ok(brandsDto);
    }

//    Get brand by id
    @GetMapping("{brandId}")
    public ResponseEntity<BrandsDto> getBrandById( @PathVariable("brandId") Long id){
    BrandsDto brandsDto = brandService.getBrandById(id);
    return ResponseEntity.ok(brandsDto);
    }

//    Update brand
    @PutMapping("{brandId}")
    public ResponseEntity<BrandsDto> updateBrand(@PathVariable("brandId") Long id, @RequestBody BrandsDto brandsDto){
        BrandsDto saveBrand = brandService.updateBrand(id, brandsDto);
        return  ResponseEntity.ok(saveBrand);
    }


//    Change status brand
    @DeleteMapping("{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable("brandId") Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.ok("Delete brand successfully");
    }
}
