package org.example.customer.controller;


import org.example.library.dto.InstrumentDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.service.InstrumentService;
import org.example.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/customer/cart")
public class CartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    private InstrumentService instrumentService;

    // Add instrument to cart
    @PostMapping("/addToCart")
    public ShoppingCartDto addToCart(@RequestBody InstrumentDto instrument, @RequestParam int quantity) {
        try {
            return shoppingCartService.addToCart(instrument, quantity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Update quantity of instrument in cart
    @PutMapping("/updateQuantity")
    public ShoppingCartDto updateQuantity(@RequestBody InstrumentDto instrument, @RequestParam int quantity) {
        try {
            return shoppingCartService.updateCart(instrument, quantity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Remove instrument from cart
    @DeleteMapping("/removeFromCart")
    public ShoppingCartDto removeFromCart(@RequestBody InstrumentDto instrument) {
        try {
            return shoppingCartService.removeFromCart(instrument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Get cart by user id
    @GetMapping("/viewCart")
    public ShoppingCartDto getCartByUserId(@RequestParam Long userId) {
        try {
            return shoppingCartService.getCart();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
