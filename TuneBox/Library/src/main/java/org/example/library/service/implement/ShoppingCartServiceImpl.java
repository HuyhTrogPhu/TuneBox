package org.example.library.service.implement;

import jakarta.servlet.http.HttpSession;
import org.example.library.dto.CartItemDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    HttpSession session;

    @Override
    public ShoppingCartDto addToCart(InstrumentDto instrumentDto, int quantity) {
        ShoppingCartDto shoppingCart = (ShoppingCartDto) session.getAttribute("cart");
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCartDto();
            session.setAttribute("cart", shoppingCart);
        }

        Set<CartItemDto> cartItemDtoSet = shoppingCart.getCartItems();
        if (cartItemDtoSet == null) {
            cartItemDtoSet = new HashSet<>();
        }

        CartItemDto cartItemDto = findDto(shoppingCart, instrumentDto.getId());
        double unitPrice = instrumentDto.getCostPrice();
        int itemQuantity;

        if (cartItemDto == null) {
            cartItemDto = new CartItemDto();
            cartItemDto.setInstrument(instrumentDto);
            cartItemDto.setUnitPrice(unitPrice);
            cartItemDto.setQuantity(quantity);
            cartItemDtoSet.add(cartItemDto);
        } else {
            itemQuantity = cartItemDto.getQuantity() + quantity;
            cartItemDto.setQuantity(itemQuantity);
        }

        shoppingCart.setCartItems(cartItemDtoSet);
        updateCartDtoTotals(shoppingCart);
        return shoppingCart;

    }

    private void updateCartDtoTotals(ShoppingCartDto shoppingCart) {
        Set<CartItemDto> cartItems = shoppingCart.getCartItems();
        shoppingCart.setTotalPrice(totalPriceDto(cartItems));
        shoppingCart.setTotalQuantity(totalItemsDto(cartItems));
    }

    private int totalItemsDto(Set<CartItemDto> cartItems) {
        int totalItems = 0;
        for (CartItemDto item : cartItems) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    private double totalPriceDto(Set<CartItemDto> cartItems) {
        double totalPrice = 0;
        for (CartItemDto item : cartItems) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    @Override
    public ShoppingCartDto updateCart(InstrumentDto instrumentDto, int quantity) {
        ShoppingCartDto shoppingCartDto = (ShoppingCartDto) session.getAttribute("cart");
        if (shoppingCartDto == null) {
            return null;
        }
        CartItemDto cartItemDto = findDto(shoppingCartDto, instrumentDto.getId());
        if (cartItemDto != null) {
            cartItemDto.setQuantity(quantity);
        }

        updateCartDtoTotals(shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto removeFromCart(InstrumentDto instrumentDto) {
        ShoppingCartDto shoppingCartDto = (ShoppingCartDto) session.getAttribute("cart");
        if (shoppingCartDto == null) {
            return null;
        }
        Set<CartItemDto> cartItemDtoSet = shoppingCartDto.getCartItems();
        CartItemDto cartItemDto = findDto(shoppingCartDto, instrumentDto.getId());
        if (cartItemDto != null) {
            cartItemDtoSet.remove(cartItemDto);
        }
        updateCartDtoTotals(shoppingCartDto);
        session.setAttribute("cart", shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto getCart() {
        return (ShoppingCartDto) session.getAttribute("cart");
    }

    public CartItemDto findDto(ShoppingCartDto shoppingCartDto, Long instrumentId) {
        if (shoppingCartDto == null) {
            return null;
        } else {
            for (CartItemDto item : shoppingCartDto.getCartItems()) {
                if (Objects.equals(item.getInstrument().getId(), instrumentId)) {
                    return item;
                }
            }
            return null;
        }
    }
}
