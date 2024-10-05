package org.example.library.service.implement;


import lombok.AllArgsConstructor;
import org.example.library.dto.BrandsDto;
import org.example.library.mapper.BrandMapper;
import org.example.library.model.Brand;
import org.example.library.repository.BrandRepository;
import org.example.library.service.BrandService;
import org.example.library.utils.ImageUploadBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

    @Autowired
    public BrandRepository brandRepository;

    private final ImageUploadBrand imageUploadBrand;

    @Override
    public BrandsDto createBrand(BrandsDto brandsDto, MultipartFile image) {
        try {
            Brand brand = BrandMapper.maptoBrand(brandsDto);
            if (image != null) {
                imageUploadBrand.uploadFile(image);
                brand.setBrandImage(Base64.getEncoder().encodeToString(image.getBytes()));
            }
            brand.setStatus(true);
            Brand saveBrand = brandRepository.save(brand);
            return BrandMapper.maptoBrandsDto(saveBrand);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
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
    public BrandsDto updateBrand(Long id, BrandsDto brandsDto, MultipartFile image) {
        try {
            // Tìm thương hiệu cần cập nhật
            Brand brand = brandRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Brand not found")
            );

            // Cập nhật tên và mô tả
            brand.setName(brandsDto.getName());
            brand.setDescription(brandsDto.getDescription());
            brand.setStatus(brandsDto.getStatus());

            // Kiểm tra xem hình ảnh có được truyền vào không
            if (image != null && !image.isEmpty()) {
                // Tải lên hình ảnh mới
                imageUploadBrand.uploadFile(image);
                brand.setBrandImage(Base64.getEncoder().encodeToString(image.getBytes()));
            }

            // Lưu thương hiệu đã cập nhật
            Brand saveBrand = brandRepository.save(brand);
            return BrandMapper.maptoBrandsDto(saveBrand);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update brand image: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage() + ", cause: " + e.getCause());
        }
    }



    @Override
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Brand not found")
        );
        brand.setStatus(false);
        brandRepository.save(brand);
    }

    @Override
    public List<BrandsDto> searchBrand(String keyword) {
        List<Brand> list = brandRepository.findByKeyword(keyword);
        return list.stream().map(BrandMapper::maptoBrandsDto).collect(Collectors.toList());
    }
    @Override
    public Brand getManagedBrand(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
    }


}
