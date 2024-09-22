package org.example.library.service;

import org.example.library.dto.BrandsDto;

import java.util.List;

public interface BrandService {
    BrandsDto createBrand(BrandsDto brandsDto);

    BrandsDto getBrandById(Long id);

    List<BrandsDto> getAllBrand();

    BrandsDto updateBrand(Long id, BrandsDto brandsDto);

    void deleteBrand(Long id);
}
