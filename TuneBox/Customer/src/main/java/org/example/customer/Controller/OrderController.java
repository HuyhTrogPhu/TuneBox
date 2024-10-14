package org.example.customer.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.example.library.dto.OrderDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.dto.UserDto;
import org.example.library.model.User;
import org.example.library.service.implement.OrderServiceImpl;
import org.example.library.service.implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/checkout")
public class OrderController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private OrderServiceImpl orderService;

    // Get user information from cookie
    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return null;
    }

    // Create a new order
    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestBody ShoppingCartDto shoppingCartDto, OrderDto orderDto, HttpServletRequest request) {
        orderService.saveOrder(shoppingCartDto, orderDto, request);
        return ResponseEntity.ok("Order created successfully!");
    }


    @GetMapping("/getOrdersByUserId/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDto> orders = null;
        return ResponseEntity.ok(orders);
    }

}
