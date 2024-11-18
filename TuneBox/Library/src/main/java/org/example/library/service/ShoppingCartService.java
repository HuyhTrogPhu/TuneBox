package org.example.library.service;

import jakarta.servlet.http.HttpSession;
import org.example.library.dto.CartItemDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.model.Instrument;

import java.util.List;

public interface ShoppingCartService {

//    ShoppingCartDto getCart(HttpSession session);

//    void addItemToCart(HttpSession session, Instrument newItem, int quantity);

//    void updateCart(HttpSession session, List<CartItemDto> items);
//
//    void removeItemFromCart(HttpSession session, Long itemId);

    ShoppingCartDto getCartForUser(Long userId);
    void saveCart(Long userId, ShoppingCartDto cartDto);
}
