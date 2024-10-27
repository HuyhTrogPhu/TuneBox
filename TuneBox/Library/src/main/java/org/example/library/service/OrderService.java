package org.example.library.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.library.dto.*;
import org.example.library.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    List<OrderListDto> getOrderList();

    List<UserIsInvoice> getOrderByUserId(Long userId);

    Order getOrderById(Long orderId);

    OrderDto createOrder(OrderDto orderDto, Long userId);

    OrderDetailInfoDto getOrderDetailByOrderId(Long orderId);

    void updateOrderStatus(Long orderId, String status, LocalDate deliveryDate, String paymentStatus );

}
