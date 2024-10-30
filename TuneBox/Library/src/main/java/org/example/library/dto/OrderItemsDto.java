package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDto {
    private Long orderDetailId;
    private int quantity;
    private Long instrumentId;
    private String instrumentName;
    private String instrumentImage;
    private double costPrice;
    private Long categoryId;
    private Long brandId;

}
