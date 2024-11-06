package org.example.library.service;

import org.example.library.dto.BrandSalesDto;
import org.example.library.dto.BrandsDto;
import org.example.library.dto.StatisticalBrandDto;
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

    // get list brand sales the most of day
    List<BrandSalesDto> getBrandSalesTheMost();

    // get list brand sales the most of week
    List<BrandSalesDto> getBrandSalesTheMostOfWeek();

    // get list brand sales the most of month
    List<BrandSalesDto> getBrandSalesTheMostOfMonth();

    // get list brand sales the least of day
    List<BrandSalesDto> getBrandSalesTheLeast();

    // get list brand sales the least of week
    List<BrandSalesDto> getBrandSalesTheLeastOfWeek();

    // get list brand sales the least of month
    List<BrandSalesDto> getBrandSalesTheLeastOfMonth();

    // get name and id brand
    List<StatisticalBrandDto> getIdsAndNamesBrand();

    // get revenue brand by brand id of day
    Double getRevenueByBrandIdOfDay(Long brandId);

    // get revenue brand by brand id of week
    Double getRevenueByBrandIdOfWeek(Long brandId);

    // get revenue brand by brand id of month
    Double getRevenueByBrandIdOfMonth(Long brandId);

    // get revenue brand by brand id of year
    Double getRevenueByBrandIdOfYear(Long brandId);
}


