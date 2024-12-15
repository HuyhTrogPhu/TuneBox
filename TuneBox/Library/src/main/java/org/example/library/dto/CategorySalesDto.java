package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySalesDto {

    private Long categoryId;

    private String categoryName;

    private String instrumentName;

    private String image;

    private double costPrice;

    private int quantity;

    private Long totalSold;
}
