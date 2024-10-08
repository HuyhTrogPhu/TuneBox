package org.example.library.service;

import org.example.library.dto.BrandsDto;
import org.example.library.model.Brand;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface    BrandService {

    BrandsDto createBrand(BrandsDto brandsDto, MultipartFile image);

    BrandsDto getBrandById(Long id);

    List<BrandsDto> getAllBrand();

    BrandsDto updateBrand(Long id, BrandsDto brandsDto, MultipartFile image);

    void deleteBrand(Long id);

    List<BrandsDto> searchBrand(String keyword);

    Brand getManagedBrand(Long id);

    List<BrandsDto> getSortedBrand();


}
