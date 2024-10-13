package org.example.library.service.implement;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.library.dto.CartItemDto;
import org.example.library.dto.OrderDto;
import org.example.library.dto.ShoppingCartDto;
import org.example.library.model.Instrument;
import org.example.library.model.Order;
import org.example.library.model.OrderDetail;
import org.example.library.model.User;
import org.example.library.repository.InstrumentRepository;
import org.example.library.repository.OrderDetailRepository;
import org.example.library.repository.OrderRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Order saveOrder(ShoppingCartDto shoppingCartDto, OrderDto orderDto, HttpServletRequest request) {

        try {
            // Lấy userId từ Cookie
            Long userId = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("userId".equals(cookie.getName())) {
                        userId = Long.valueOf(cookie.getValue());
                        break;
                    }
                }
            }

            if (userId == null) {
                throw new IllegalArgumentException("User ID not found in cookies");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id"));

            // Tạo mới đơn hàng
            Order order = new Order();
            order.setOrderDate(LocalDate.now());
            order.setDeliveryDate(null);  // Chưa có ngày giao hàng
            order.setTotalItems(shoppingCartDto.getItems().size());
            order.setTotalPrice(shoppingCartDto.getTotalPrice());
            order.setPaymentMethod(orderDto.getPaymentMethod());
            order.setStatus("Pending");
            order.setUser(user);

            // Tạo danh sách chi tiết đơn hàng
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (CartItemDto item : shoppingCartDto.getItems()) {
                // Tạo chi tiết đơn hàng
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setOrder(order);
                orderDetail.setInstrument(item.getInstrument());

                // Lưu chi tiết đơn hàng
                orderDetailRepository.save(orderDetail);
                orderDetails.add(orderDetail);

                // Cập nhật số lượng nhạc cụ sau khi thanh toán
                Instrument instrument = instrumentRepository.findById(item.getInstrument().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Instrument not found with id: " + item.getInstrument().getId()));
                int updatedQuantity = instrument.getQuantity() - item.getQuantity();
                instrument.setQuantity(updatedQuantity);
                instrumentRepository.save(instrument);
            }

            // Lưu danh sách chi tiết vào đơn hàng
            order.setOrderDetails(orderDetails);
            // Lưu và trả về đơn hàng
            return orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }
        return order;
    }

    @Override
    public Order updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getOrderId())
               .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderDto.getOrderId()));

        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setStatus(orderDto.getStatus());
        return orderRepository.save(order);
    }
}
