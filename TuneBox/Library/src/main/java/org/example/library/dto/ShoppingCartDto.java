package org.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {

    private Long shoppingCartId;

    private UserDto user;

    private double totalPrice;

    private int totalQuantity;

    private Set<CartItemDto> cartItems;

}
