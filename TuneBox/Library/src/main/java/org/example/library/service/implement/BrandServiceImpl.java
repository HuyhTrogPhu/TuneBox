package org.example.library.service.implement;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.example.library.dto.BrandSalesDto;
import org.example.library.dto.BrandsDto;
import org.example.library.dto.StatisticalBrandDto;
import org.example.library.mapper.BrandMapper;
import org.example.library.model.Brand;
import org.example.library.repository.BrandRepository;
import org.example.library.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

    @Autowired
    public BrandRepository brandRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Override
    public BrandsDto createBrand(BrandsDto brandsDto, MultipartFile image) {
        try {

            Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl  = (String) uploadResult.get("url");
            brandsDto.setBrandImage(imageUrl );

            Brand brand = new Brand();
            brand.setName(brandsDto.getName());
            brand.setDescription(brandsDto.getDescription());
            brand.setBrandImage(imageUrl);
            brand.setStatus(false);
            brandRepository.save(brand);
            return brandsDto;
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
//            if (image != null && !image.isEmpty()) {
//                // Tải lên hình ảnh mới
//                imageUploadBrand.uploadFile(image);
//                brand.setBrandImage(Base64.getEncoder().encodeToString(image.getBytes()));
//            }

            if (image != null && !image.isEmpty()) {
                // Xóa ảnh cũ trên Cloudinary (nếu cần)
                if (brand.getBrandImage() != null) {
                    String publicId = extractPublicIdFromUrl(brand.getBrandImage());
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                }

                // Tải ảnh mới lên Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("url");

                // Cập nhật URL của ảnh mới
                brand.setBrandImage(imageUrl);
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

    private String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String publicIdWithExtension = parts[parts.length - 1];
        return publicIdWithExtension.split("\\.")[0];  // Lấy publicId trước phần mở rộng ảnh (ví dụ: .jpg)
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

    @Override
    public List<BrandSalesDto> getBrandSalesTheMost() {
        return brandRepository.getBrandSalesTheMostOdDay();
    }

    @Override
    public List<BrandSalesDto> getBrandSalesTheMostOfWeek() {
        return brandRepository.getBrandSalesTheMostOfWeek();
    }

    @Override
    public List<BrandSalesDto> getBrandSalesTheMostOfMonth() {
        return brandRepository.getBrandSalesTheMostOfMonth();
    }

    @Override
    public List<BrandSalesDto> getBrandSalesTheLeast() {
        return brandRepository.getBrandSalesTheLeastOdDay();
    }

    @Override
    public List<BrandSalesDto> getBrandSalesTheLeastOfWeek() {
        return brandRepository.getBrandSalesTheLeastOfWeek();
    }

    @Override
    public List<BrandSalesDto> getBrandSalesTheLeastOfMonth() {
        return brandRepository.getBrandSalesTheLeastOfMonth();
    }

    @Override
    public List<StatisticalBrandDto> getIdsAndNamesBrand() {
        return brandRepository.getStatisticalBrand();
    }

    @Override
    public Double getRevenueByBrandIdOfDay(Long brandId) {
        return brandRepository.getRevenueByBrandIdOfDay(brandId);
    }

    @Override
    public Double getRevenueByBrandIdOfWeek(Long brandId) {
        return brandRepository.getRevenueByBrandIdOfWeek(brandId);
    }

    @Override
    public Double getRevenueByBrandIdOfMonth(Long brandId) {
        return brandRepository.getRevenueByBrandIdOfMonth(brandId);
    }

    @Override
    public Double getRevenueByBrandIdOfYear(Long brandId) {
        return brandRepository.getRevenueByBrandIdOfYear(brandId);
    }


}
