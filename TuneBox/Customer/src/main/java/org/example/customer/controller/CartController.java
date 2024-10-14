package org.example.customer.controller;

import jakarta.servlet.http.HttpSession;
import org.example.library.dto.CartItemDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.model.Instrument;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/cart")
public class CartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Instrument cartItem, @RequestParam int quantity, HttpSession session) {
        shoppingCartService.addItemToCart(session, cartItem, quantity);
        return ResponseEntity.ok("Đã thêm vào giỏ hàng.");
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncCart(@RequestBody List<CartItemDto> cartItems, HttpSession session) {
        shoppingCartService.updateCart(session, cartItems);
        return ResponseEntity.ok("Giỏ hàng đã được đồng bộ.");
    }

    @GetMapping("/items")
    public ResponseEntity<ShoppingCartDto> getCartItems(HttpSession session) {
        ShoppingCartDto cartDTO = shoppingCartService.getCart(session);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/remove/{instrumentId}")
    public ResponseEntity<String> removeItem(@PathVariable Long instrumentId, HttpSession session) {
        shoppingCartService.removeItemFromCart(session, instrumentId);
        return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ hàng.");
    }
}