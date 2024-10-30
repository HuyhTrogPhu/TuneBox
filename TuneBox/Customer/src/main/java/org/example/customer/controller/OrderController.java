package org.example.customer.controller;


import org.example.library.dto.OrderDto;
import org.example.library.dto.UserCheckOut;
import org.example.library.model.Order;
import org.example.library.service.UserService;
import org.example.library.service.implement.OrderServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/checkout")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderServiceImpl orderService;

    // Get user information from cookie
    @GetMapping("/getUserById/{userId}")
    public UserCheckOut getUserById(@PathVariable Long userId) {
        return null;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto,
                                                @CookieValue(value = "userId", required = false) String userIdCookie) {
        if (userIdCookie == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Hoặc xử lý lỗi theo yêu cầu của bạn
        }

        Long userId = Long.valueOf(userIdCookie);
        OrderDto createdOrder = orderService.createOrder(orderDto, userId);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }



    @GetMapping("/getOrdersByUserId/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDto> orders = null;
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrderById/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderDto orderDto = orderService.mapToDto(order);
        return ResponseEntity.ok(orderDto);
    }

}
