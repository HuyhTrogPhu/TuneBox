package org.example.customer.controller;


import org.example.library.dto.*;
import org.example.library.model.Order;
import org.example.library.service.EmailService;
import org.example.library.service.implement.OrderServiceImpl;
import org.example.library.service.implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/customer/checkout")
public class OrderController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private EmailService emailService;

    // Get user information from cookie
    @GetMapping("/getUserById/{userId}")
    public UserCheckOut getUserById(@PathVariable Long userId) {
        return userService.getUserCheckoutInfo(userId);
    }


    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto,
                                                @CookieValue(value = "userId", required = false) String userIdCookie) {
        if (userIdCookie == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Hoặc xử lý lỗi theo yêu cầu của bạn
        }

        Long userId = Long.valueOf(userIdCookie);
        OrderDto createdOrder = orderService.createOrder(orderDto, userId);

        // Định dạng totalPrice
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = currencyFormat.format(createdOrder.getTotalPrice());

        // Định dạng orderDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedOrderDate = createdOrder.getOrderDate().format(dateFormatter);

        // Lấy thông tin người dùng (email) để gửi thông báo
        UserCheckOut userCheckOut = userService.getUserCheckoutInfo(userId);

        // Tạo đường dẫn đến chi tiết hóa đơn
        String orderDetailUrl = "http://localhost:3000/orderDetail/" + createdOrder.getOrderId(); // Cập nhật đường dẫn tùy theo cấu trúc ứng dụng của bạn

        // Tạo nội dung email
        String subject = "Xác nhận đơn hàng #" + createdOrder.getOrderId();
        String body = "Xin chào " + userCheckOut.getUserName() + ",\n\n"
                + "Cảm ơn bạn đã đặt hàng tại TuneBox. Đây là thông tin đơn hàng của bạn:\n"
                + "Mã đơn hàng: " + createdOrder.getOrderId() + "\n"
                + "Tổng tiền: " + formattedTotalPrice + "\n" // Sử dụng giá trị đã định dạng
                + "Ngày đặt: " + formattedOrderDate + "\n" // Sử dụng giá trị đã định dạng
                + "Chi tiết hóa đơn: " + orderDetailUrl + "\n" // Đường dẫn đến chi tiết hóa đơn
                + "\nChúng tôi sẽ sớm xác nhận và giao hàng cho bạn.\n"
                + "Xin cảm ơn!";

        // Gửi email
        emailService.sendOrderConfirmationEmail(userCheckOut.getEmail(), subject, body);

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }





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
    @GetMapping("/getOrderById/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderDto orderDto = orderService.mapToDto(order);
        return ResponseEntity.ok(orderDto);
    }

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
