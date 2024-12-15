package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto2 {
    private Long userId;
    private List<CartItemDto> items;
    private Double totalPrice;
}
