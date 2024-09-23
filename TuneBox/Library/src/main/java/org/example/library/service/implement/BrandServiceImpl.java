package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.mapper.BrandMapper;
import org.example.library.model.Brand;
import org.example.library.repository.BrandRepository;
import org.example.library.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

    @Autowired
    public BrandRepository brandRepository;

    @Override
    public BrandsDto createBrand(BrandsDto brandsDto) {
        Brand brand = BrandMapper.maptoBrand(brandsDto);
        brand.setStatus(true);
        Brand saveBrand = brandRepository.save(brand);
        return  BrandMapper.maptoBrandsDto(saveBrand);
    }

    @Override
    public BrandsDto getBrandById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Brand not found")
        );
        return BrandMapper.maptoBrandsDto(brand);
    }

    @Override
    public List<BrandsDto> getAllBrand() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream().map(BrandMapper::maptoBrandsDto).collect(Collectors.toList());
    }

    @Override
    public BrandsDto updateBrand(Long id, BrandsDto brandsDto) {
        Brand brand = brandRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Brand not found")
        );
        brand.setName(brandsDto.getName());
        brand.setStatus(true);
        Brand saveBrand = brandRepository.save(brand);
        return BrandMapper.maptoBrandsDto(saveBrand);
    }

    @Override
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Brand not found")
        );
        brand.setStatus(false);
        brandRepository.save(brand);
    }
}
