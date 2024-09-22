package org.example.library.mapper;

import org.example.library.dto.BrandsDto;
import org.example.library.model.Brand;

public class BrandMapper {

    public static BrandsDto maptoBrandsDto(Brand brand) {
        return new BrandsDto(brand.getId(), brand.getName(), brand.isStatus());
    }

    public static Brand maptoBrand(BrandsDto brandsDto) {
        return new Brand(brandsDto.getId(), brandsDto.getName(), brandsDto.isStatus());
    }

}
