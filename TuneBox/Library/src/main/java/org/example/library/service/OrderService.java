package org.example.library.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.library.dto.OrderDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.model.Order;

import java.util.List;

public interface OrderService {

    public List<Order> getOrderList();

    public List<Order> getOrderByUserId(Long userId);

    public Order getOrderById(Long orderId);


    OrderDto createOrder(OrderDto orderDto, Long userId);


}
