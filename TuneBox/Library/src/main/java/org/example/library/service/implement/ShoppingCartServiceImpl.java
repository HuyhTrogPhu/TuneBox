package org.example.library.service.implement;

import jakarta.servlet.http.HttpSession;
import org.example.library.dto.CartItemDto;
import org.example.library.dto.InstrumentDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.service.InstrumentService;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    HttpSession session;

    @Autowired
    InstrumentService instrumentService;

    private static Long shoppingCartCounter = 1L;
    private static Long cartItemsCounter = 1L;

    @Override
    public ShoppingCartDto addToCart(Long instrumentId, int quantity) {
        // Lấy ShoppingCart từ session
        ShoppingCartDto shoppingCart = (ShoppingCartDto) session.getAttribute("cart");

        // Nếu shoppingCart chưa được khởi tạo, tạo mới nó và khởi tạo cartItems
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCartDto();
            shoppingCart.setShoppingCartId(shoppingCartCounter++);
            shoppingCart.setCartItems(new HashSet<>());
            session.setAttribute("cart", shoppingCart);
        }

        // Khởi tạo cartItems từ shoppingCart
        Set<CartItemDto> cartItemDtoSet = shoppingCart.getCartItems();

        // Tìm kiếm sản phẩm từ service
        InstrumentDto instrumentDto = instrumentService.getInstrumentById(instrumentId);
        if (instrumentDto == null) {
            throw new RuntimeException("Instrument not found");
        }

        // Tìm kiếm CartItemDto dựa trên ID của sản phẩm
        CartItemDto cartItemDto = findDto(shoppingCart, instrumentId);
        double unitPrice = instrumentDto.getCostPrice();

        // Nếu không tìm thấy sản phẩm trong giỏ hàng, tạo mới CartItemDto
        if (cartItemDto == null) {
            cartItemDto = new CartItemDto();
            cartItemDto.setCartItemId(cartItemsCounter++);
            cartItemDto.setInstrument(instrumentDto);
            cartItemDto.setUnitPrice(unitPrice);
            cartItemDto.setQuantity(quantity);
            cartItemDtoSet.add(cartItemDto); // Thêm vào giỏ hàng
        } else {
            // Nếu đã tồn tại, cập nhật số lượng
            int itemQuantity = cartItemDto.getQuantity() + quantity;
            cartItemDto.setQuantity(itemQuantity);
        }

        // Cập nhật cartItemDtoSet vào shoppingCart
        shoppingCart.setCartItems(cartItemDtoSet);
        updateCartDtoTotals(shoppingCart);

        return shoppingCart;
    }


    private void updateCartDtoTotals(ShoppingCartDto shoppingCart) {
        // Đảm bảo cartItems không null trước khi tính toán tổng
        Set<CartItemDto> cartItems = shoppingCart.getCartItems();
        if (cartItems != null) {
            shoppingCart.setTotalPrice(totalPriceDto(cartItems));
            shoppingCart.setTotalQuantity(totalItemsDto(cartItems));
        }
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
    public ShoppingCartDto updateCart(Long instrumentId, int quantity) {
        ShoppingCartDto shoppingCartDto = (ShoppingCartDto) session.getAttribute("cart");
        if (shoppingCartDto == null) {
            return null;
        }
        CartItemDto cartItemDto = findDto(shoppingCartDto, instrumentId);
        if (cartItemDto != null) {
            cartItemDto.setQuantity(quantity);
        }

        updateCartDtoTotals(shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto removeFromCart(Long instrumentId) {
        ShoppingCartDto shoppingCartDto = (ShoppingCartDto) session.getAttribute("cart");
        if (shoppingCartDto == null) {
            return null;
        }
        Set<CartItemDto> cartItemDtoSet = shoppingCartDto.getCartItems();
        CartItemDto cartItemDto = findDto(shoppingCartDto, instrumentId);
        if (cartItemDto != null) {
            cartItemDtoSet.remove(cartItemDto); // Xóa sản phẩm khỏi giỏ hàng
        }
        updateCartDtoTotals(shoppingCartDto);
        session.setAttribute("cart", shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto getCart() {
        ShoppingCartDto shoppingCart = (ShoppingCartDto) session.getAttribute("cart");
        return shoppingCart;
    }


    public CartItemDto findDto(ShoppingCartDto shoppingCartDto, Long instrumentId) {
        if (shoppingCartDto == null || shoppingCartDto.getCartItems() == null) {
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
