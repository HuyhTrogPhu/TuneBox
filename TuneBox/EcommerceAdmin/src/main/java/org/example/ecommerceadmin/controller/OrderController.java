package org.example.ecommerceadmin.controller;


import org.example.library.dto.*;
import org.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
