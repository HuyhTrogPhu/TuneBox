package org.example.library.service.implement;

import jakarta.servlet.http.HttpSession;

import org.example.library.dto.CartItemDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.model.CartItem;
import org.example.library.model.Instrument;
import org.example.library.model.ShoppingCart;
import org.example.library.model.User;
import org.example.library.repository.ShoppingCartRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final long cartIdCounter = 1;
@Autowired
     ShoppingCartRepository shoppingCartRepository;
@Autowired
   UserRepository userRepository;



    @Override
    public ShoppingCartDto getCartForUser(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        List<CartItemDto> items = cart.getItems().stream()
                .map(item -> new CartItemDto(
                        item.getInstrumentId(),
                        item.getName(),
                        item.getImage(),
                        item.getCostPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new ShoppingCartDto(userId, items, cart.getTotalPrice());
    }

    @Override
    public void saveCart(Long userId, ShoppingCartDto cartDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        // Xóa giỏ hàng cũ
        cart.getItems().clear();

        // Thêm các mục mới
        for (CartItemDto itemDto : cartDto.getItems()) {
            CartItem item = new CartItem();
            item.setShoppingCart(cart);
            item.setInstrumentId(itemDto.getInstrumentId());
            item.setName(itemDto.getName());
            item.setImage(itemDto.getImage());
            item.setCostPrice(itemDto.getCostPrice());
            item.setQuantity(itemDto.getQuantity());
            cart.getItems().add(item);
        }

        // Cập nhật tổng giá
        cart.setTotalPrice(cartDto.getItems().stream()
                .mapToDouble(item -> item.getCostPrice() * item.getQuantity())
                .sum());

        shoppingCartRepository.save(cart);
    }


    private ShoppingCart createNewCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setTotalPrice(0.0);
        shoppingCartRepository.save(cart);
        return cart;
    }
}
