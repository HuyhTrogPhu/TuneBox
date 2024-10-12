package org.example.customer.controller;

import jakarta.servlet.http.HttpSession;
import org.example.library.model.Instrument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer/cart")
public class CartController {

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Instrument cartItem, HttpSession session) {
        List<Instrument> cart = (List<Instrument>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        boolean itemExists = false;
        for (Instrument item : cart) {
            if (item.getId().equals(cartItem.getId())) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                item.setColor(item.getColor());
                item.setName(item.getName());
                item.setImage(item.getImage());
                item.setCostPrice(item.getCostPrice());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            cart.add(cartItem);
        }

        // Cập nhật session với giỏ hàng mới
        session.setAttribute("cart", cart);

        return ResponseEntity.ok("Đã thêm vào giỏ hàng.");
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncCart(@RequestBody List<Instrument> cartItems, HttpSession session) {
        List<Instrument> cart = (List<Instrument>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Đồng bộ giỏ hàng từ frontend lên server
        for (Instrument newItem : cartItems) {
            boolean itemExists = false;
            for (Instrument currentItem : cart) {
                if (currentItem.getId().equals(newItem.getId())) {
                    currentItem.setQuantity(newItem.getQuantity());
                    currentItem.setName(newItem.getName());
                    currentItem.setCostPrice(newItem.getCostPrice());
                    currentItem.setImage(newItem.getImage());
                    currentItem.setColor(newItem.getColor());
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                cart.add(newItem);
            }
        }

        session.setAttribute("cart", cart);
        return ResponseEntity.ok("Giỏ hàng đã được đồng bộ.");
    }

    @GetMapping("/items")
    public ResponseEntity<List<Instrument>> getCartItems(HttpSession session) {
        List<Instrument> cart = (List<Instrument>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{instrumentId}")
    public ResponseEntity<String> removeItem(@PathVariable Long instrumentId, HttpSession session) {
        List<Instrument> cart = (List<Instrument>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getId().equals(instrumentId));
            session.setAttribute("cart", cart);
        }
        return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ hàng.");
    }
}
