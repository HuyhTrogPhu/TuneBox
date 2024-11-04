package org.example.ecommerceadmin.controller;


import org.example.library.dto.*;
import org.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/e-order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    // get list orders by user id
    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<UserIsInvoice>> getAllOrdersByUserId(@PathVariable Long userId) {
        try {
            List<UserIsInvoice> userIsInvoices = orderService.getOrderByUserId(userId);
            return ResponseEntity.ok(userIsInvoices);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    // get list orders
    @GetMapping("/orders")
    public ResponseEntity<List<OrderListDto>> getAllOrders() {
        try {
            List<OrderListDto> userIsInvoices = orderService.getOrderList();
            return ResponseEntity.ok(userIsInvoices);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    // get order detail by orderId
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDetailInfoDto> getOrderDetailByOrderId(@PathVariable Long orderId) {
        try {
            OrderDetailInfoDto orderDetailInfoDto = orderService.getOrderDetailByOrderId(orderId);
            return ResponseEntity.ok(orderDetailInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    // Update order status
    @PutMapping("/order/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderListDto orderListDto) {
        try {
            orderService.updateOrderStatus(orderId, orderListDto.getStatus(), orderListDto.getDeliveryDate(), orderListDto.getPaymentStatus());
            return ResponseEntity.ok("Order status updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order status");
        }
    }



}
