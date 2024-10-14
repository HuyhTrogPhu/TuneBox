package org.example.customer.controller;

import org.example.library.dto.InstrumentDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.service.InstrumentService;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/cart")
public class CartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    private InstrumentService instrumentService;

    // Add instrument to cart
    @PostMapping("/addToCart")
    public ShoppingCartDto addToCart(@RequestParam Long instrumentId, @RequestParam int quantity) {
        try {
            return shoppingCartService.addToCart(instrumentId, quantity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Update quantity of instrument in cart
    @PutMapping("/updateQuantity")
    public ShoppingCartDto updateQuantity(@RequestParam Long instrumentId, @RequestParam int quantity) {
        try {
            return shoppingCartService.updateCart(instrumentId, quantity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Remove instrument from cart
    @DeleteMapping("/removeFromCart")
    public ShoppingCartDto removeFromCart(@RequestParam Long  instrumentId) {
        try {
            return shoppingCartService.removeFromCart(instrumentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Get cart by user id
    @GetMapping("/viewCart")
    public ShoppingCartDto getCartByUserId() {
        try {
            return shoppingCartService.getCart();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
