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

//    List brand a, b, c...
    List<BrandsDto> getAllBrandA();

    List<BrandsDto> getAllBrandB();

    List<BrandsDto> getAllBrandC();

    List<BrandsDto> getAllBrandD();

    List<BrandsDto> getAllBrandE();

    List<BrandsDto> getAllBrandF();

    List<BrandsDto> getAllBrandG();

    List<BrandsDto> getAllBrandH();

    List<BrandsDto> getAllBrandI();

    List<BrandsDto> getAllBrandJ();

    List<BrandsDto> getAllBrandK();

    List<BrandsDto> getAllBrandL();

    List<BrandsDto> getAllBrandM();

    List<BrandsDto> getAllBrandN();

    List<BrandsDto> getAllBrandO();

    List<BrandsDto> getAllBrandP();

    List<BrandsDto> getAllBrandQ();

    List<BrandsDto> getAllBrandR();

    List<BrandsDto> getAllBrandS();

    List<BrandsDto> getAllBrandT();

    List<BrandsDto> getAllBrandU();

    List<BrandsDto> getAllBrandV();

    List<BrandsDto> getAllBrandW();

    List<BrandsDto> getAllBrandX();

    List<BrandsDto> getAllBrandY();

    List<BrandsDto> getALlBrandZ();
}
