package org.example.customer.controller;


import lombok.AllArgsConstructor;
import org.example.library.dto.CartRequest;
import org.example.library.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/customer/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add to cart
    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest request) {
        try {
            cartService.addToCart(request.getInstrumentId(), request.getQuantity());
            return ResponseEntity.ok("Added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add to cart: " + e.getMessage());
        }
    }

    //  Update to cart
    @PutMapping("/updateCart")
    public ResponseEntity<String> updateCart(@RequestBody CartRequest request) {
        cartService.updateQuantity(request.getInstrumentId(), request.getQuantity());
        return ResponseEntity.ok("Updated cart successfully");
    }

    // Delete from cart
    @DeleteMapping("/deleteCart/{instrumentId}")
    public ResponseEntity<String> deleteFromCart(@PathVariable Long instrumentId) {
        cartService.removeItem(instrumentId);
        return ResponseEntity.ok("Deleted from cart successfully");
    }

    // Get cart
    @GetMapping("/items")
    public ResponseEntity<Map<Long, Integer>> getCartItems() {
        return ResponseEntity.ok(cartService.getCart());
    }

    //  Clear cart
    @DeleteMapping("/clearCart")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
