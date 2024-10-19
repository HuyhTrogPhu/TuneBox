package org.example.library.service.implement;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.library.dto.CartItemDto;
import org.example.library.dto.OrderDetailDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + orderId));
    }


    @Override
    public OrderDto createOrder(OrderDto orderDto, Long userId) {
        System.out.println("Creating order for user ID: " + userId);
        System.out.println("Order data: " + orderDto);

        // Lấy thông tin người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setOrderDate(orderDto.getOrderDate());
        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setTax(orderDto.getTax());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setTotalItems(orderDto.getTotalItem());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setStatus(orderDto.getStatus());
        order.setAddress(orderDto.getAddress());
        order.setShippingMethod(orderDto.getShippingMethod());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setUser(user);

        try {
            // Xử lý các chi tiết đơn hàng
            List<OrderDetail> orderDetails = orderDto.getOrderDetails().stream().map(detailDto -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(detailDto.getQuantity());

                // Lấy nhạc cụ từ database
                Instrument instrument = instrumentRepository.findById(detailDto.getInstrumentId())
                        .orElseThrow(() -> new RuntimeException("Instrument not found with ID: " + detailDto.getInstrumentId()));
                orderDetail.setInstrument(instrument);
                orderDetail.setOrder(order);
                return orderDetail;
            }).collect(Collectors.toList());

            order.setOrderDetails(orderDetails);

            // Lưu đơn hàng vào cơ sở dữ liệu
            Order savedOrder = orderRepository.save(order);

            // Chuyển đổi dữ liệu để trả về DTO
            return mapToDto(savedOrder);
        } catch (RuntimeException e) {
            // Ghi lại lỗi vào log để kiểm tra
            System.err.println("Error creating order: " + e.getMessage());
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    // Hàm hỗ trợ chuyển đổi từ Order sang OrderDto
    public OrderDto mapToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setDeliveryDate(order.getDeliveryDate());
        orderDto.setTax(order.getTax());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setTotalItem(order.getTotalItems());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        orderDto.setPhoneNumber(order.getPhoneNumber());
        orderDto.setStatus(order.getStatus());
        orderDto.setAddress(order.getAddress());
        orderDto.setShippingMethod(order.getShippingMethod());
        orderDto.setUserId(order.getUser().getId());

        orderDto.setUsername(order.getUser().getUserName());
        orderDto.setEmail(order.getUser().getEmail());
        List<OrderDetailDto> orderDetailDtos = order.getOrderDetails().stream().map(detail -> {
            OrderDetailDto detailDto = new OrderDetailDto();
            detailDto.setId(detail.getId());
            detailDto.setQuantity(detail.getQuantity());
            detailDto.setInstrumentId(detail.getInstrument().getId());
            detailDto.setInstrumentName(detail.getInstrument().getName());
            detailDto.setImage(detail.getInstrument().getImage());
            detailDto.setCostPrice(String.valueOf(detail.getInstrument().getCostPrice()));
            return detailDto;
        }).collect(Collectors.toList());

        orderDto.setOrderDetails(orderDetailDtos);

        return orderDto;
    }
    public boolean updatePaymentStatus(Long orderId, String paymentStatus) {
        // Logic cập nhật trạng thái thanh toán vào CSDL
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId)); // Sử dụng orElseThrow để lấy giá trị

        // Cập nhật trạng thái thanh toán
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
        return true;
    }



}