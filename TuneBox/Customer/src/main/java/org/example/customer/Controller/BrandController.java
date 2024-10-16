package org.example.customer.Controller;

import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.service.implement.BrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/customer/brand")
public class BrandController {
    @Autowired
    private BrandServiceImpl brandService;

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
}
