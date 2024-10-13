package org.example.customer.controller;


import org.example.library.dto.UserDto;
import org.example.library.service.implement.OrderServiceImpl;
import org.example.library.service.implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/checkout")
public class OrderController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUserById(@RequestParam Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/createOrder")
    public ResponseEntity<String> createOrder() {
        // Implement logic to create an order
        return ResponseEntity.ok("Order created successfully!");
    }

    @GetMapping("/getOrdersByUserId/{userId}")
    public ResponseEntity<String> getOrdersByUserId(@PathVariable Long userId) {
        // Implement logic to get orders by user ID
        return ResponseEntity.ok("Orders retrieved successfully!");
    }

}
