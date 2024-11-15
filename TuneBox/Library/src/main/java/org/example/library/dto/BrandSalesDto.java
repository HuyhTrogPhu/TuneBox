package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandSalesDto {

    private Long brandId;

    private String brandName;

    private String instrumentName;

    private String image;

    private double costPrice;

    private int quantity;

    private Long totalSold;
}
