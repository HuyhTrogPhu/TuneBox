package org.example.library.service;

import org.example.library.dto.InstrumentDto;
import org.example.library.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto addToCart(InstrumentDto instrumentDto, int quantity);

    ShoppingCartDto updateCart(InstrumentDto instrumentDto, int quantity);

    ShoppingCartDto removeFromCart(InstrumentDto instrumentDto);

    ShoppingCartDto getCart();
}
