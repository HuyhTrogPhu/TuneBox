package org.example.customer.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.customer.config.VNPayConfig;
import org.example.library.dto.OrderDto;
import org.example.library.dto.PaymentRestDTO;
import org.example.library.dto.UserCheckOut;
import org.example.library.service.EmailService;
import org.example.library.service.OrderService;
import org.example.library.service.implement.OrderServiceImpl;
import org.example.library.service.implement.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/customer/checkout")
public class VNPayController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderServiceImpl orderServiceImpl;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserServiceImpl userService;
    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestBody  OrderDto orderDto, @CookieValue(value = "userId", required = false) String userIdCookie
            , HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println("createPayment method called");
        System.out.println("Order Data: " + orderDto);

        Long userId = Long.valueOf(userIdCookie);
        OrderDto createdOrder = orderService.createOrder(orderDto, userId);

        long amount = (long) orderDto.getTotalPrice();
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Nhân với 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // Tính toán giá trị SecureHash
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(URLEncoder.encode(vnp_SecureHash, StandardCharsets.US_ASCII.toString()));
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query.toString();

        //gửi mail xác nhan
        UserCheckOut userCheckOut = userService.getUserCheckoutInfo(userId);
        String orderDetailUrl = "http://localhost:3000/orderDetail/" + createdOrder.getOrderId();
        String subject = "Xác nhận đơn hàng #" + createdOrder.getOrderId();
        String body = "Xin chào " + userCheckOut.getUserName() + ",\n\n"
                + "Cảm ơn bạn đã đặt hàng tại TuneBox. Đây là thông tin đơn hàng của bạn:\n"
                + "Mã đơn hàng: " + createdOrder.getOrderId() + "\n"
                + "Tổng tiền: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(createdOrder.getTotalPrice()) + "\n"
                + "Ngày đặt: " + createdOrder.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n"
                +  "Chi tiết hóa đơn: " + orderDetailUrl + "\n"
                + "Vui lòng truy cập vào VNPay để hoàn tất thanh toán tại link sau: " + paymentUrl + "\n"
                + "\nXin cảm ơn!";

        emailService.sendOrderConfirmationEmail(userCheckOut.getEmail(), subject, body);


        // trả về url thanh toán cho fe
        PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
        paymentRestDTO.setStatus("OK");
        paymentRestDTO.setMessage("Successfully");
        paymentRestDTO.setURL(paymentUrl);
        System.out.println("paymentUrl: " + paymentUrl);
        System.out.println("vnp_SecureHash: " + vnp_SecureHash);
        System.out.println("vnp_Params: " + vnp_Params);
        return ResponseEntity.ok(paymentRestDTO);
    }

    @PostMapping("/update_payment_status")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody Map<String, String> request) {
        String orderIdString = request.get("orderId");
        String paymentStatus = request.get("paymentStatus");

        // Chuyển đổi orderId từ String sang Long
        Long orderId;
        try {
            orderId = Long.parseLong(orderIdString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order ID không hợp lệ");
        }

        // Cập nhật trạng thái thanh toán
        boolean success = orderServiceImpl.updatePaymentStatus(orderId, paymentStatus);

        if (success) {
            return ResponseEntity.ok("Cập nhật trạng thái thanh toán thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cập nhật trạng thái thanh toán thất bại");
        }
    }


}