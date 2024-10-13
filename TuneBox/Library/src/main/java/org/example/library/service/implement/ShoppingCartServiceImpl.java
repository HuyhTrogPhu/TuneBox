package org.example.library.service.implement;

import jakarta.servlet.http.HttpSession;

import org.example.library.dto.CartItemDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.model.Instrument;
import org.example.library.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;


@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static long cartIdCounter = 1;

    @Override
    public ShoppingCartDto getCart(HttpSession session) {
        ShoppingCartDto cart = (ShoppingCartDto) session.getAttribute("cart");

        if (cart == null) {
            // Tạo giỏ hàng mới với cartId tự động tăng
            cart = new ShoppingCartDto(cartIdCounter++, new ArrayList<>(), 0.0);
            session.setAttribute("cart", cart);
        }

        double totalPrice = cart.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getInstrument().getCostPrice()).sum();
        cart.setTotalPrice(totalPrice); // Cập nhật tổng giá trị giỏ hàng
        return cart;
    }
    @Override
    public void addItemToCart(HttpSession session, Instrument newItem, int quantity) {
        ShoppingCartDto cart = getCart(session); // Lấy giỏ hàng từ session

        // Kiểm tra nếu sản phẩm đã tồn tại trong giỏ hàng
        boolean itemExists = false;
        for (CartItemDto item : cart.getItems()) {
            if (item.getInstrument().getId().equals(newItem.getId())) {
                // Nếu sản phẩm tồn tại, tăng số lượng sản phẩm
                item.setQuantity(item.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
        if (!itemExists) {
            cart.getItems().add(new CartItemDto(newItem, quantity));
        }

        // Cập nhật lại session với giỏ hàng đã thay đổi
        session.setAttribute("cart", cart);
    }
    @Override
    public void updateCart(HttpSession session, List<CartItemDto> items) {
        ShoppingCartDto cart = getCart(session);
        cart.setItems(items);
        session.setAttribute("cart", cart);
    }
    @Override
    public void removeItemFromCart(HttpSession session, Long itemId) {
        ShoppingCartDto cart = getCart(session);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getInstrument().getId().equals(itemId));
            session.setAttribute("cart", cart);
        }
    }
}
