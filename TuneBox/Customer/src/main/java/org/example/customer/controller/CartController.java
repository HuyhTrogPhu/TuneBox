package org.example.customer.controller;

import jakarta.servlet.http.HttpSession;
import org.example.library.dto.CartItemDto;
import org.example.library.dto.OrderDetailDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.model.Instrument;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/cart")
public class CartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ShoppingCartDto> getCart(@PathVariable Long userId) {
        ShoppingCartDto cart = shoppingCartService.getCartForUser(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> saveCart(@PathVariable Long userId, @RequestBody ShoppingCartDto cartDto) {
        shoppingCartService.saveCart(userId, cartDto);
        return ResponseEntity.ok("Cart saved successfully.");
    }


    private final List<OrderDetailDto> orderDetails = new ArrayList<>();
}