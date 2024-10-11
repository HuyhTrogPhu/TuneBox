package org.example.library.service;

import org.example.library.dto.InstrumentDto;
import org.example.library.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto addToCart(Long instrumentId, int quantity);

    ShoppingCartDto updateCart(Long instrumentId, int quantity);

    ShoppingCartDto removeFromCart(Long instrumentId);

    ShoppingCartDto getCart();
}
